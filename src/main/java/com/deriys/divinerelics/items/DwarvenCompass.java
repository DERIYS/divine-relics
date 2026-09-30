package com.deriys.divinerelics.items;

import com.deriys.divinerelics.structures.StructureLocator;
import com.deriys.divinerelics.util.custom.StructureName;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * Points to the nearest Brok and Sindri Shop / Ancient Dwarven Mines.
 * <p>
 * Searches run asynchronously through {@link StructureLocator}: the stack only stores a search ticket and is polled
 * every {@link #POLL_INTERVAL} ticks, so neither using the compass nor carrying one can stall the server thread.
 * The old implementation ran a synchronous vanilla locate (which generates chunks) on use and on <i>every tick</i>
 * while no structure was known, which froze servers in dimensions or areas without a reachable structure.
 */
public class DwarvenCompass extends Item {
    public final static StructureName BROK_AND_SINDRI_SHOP = new StructureName("divinerelics:brok_and_sindri_shop", "Brok and Sindri Shop");
    public final static StructureName DWARVEN_MINES = new StructureName("divinerelics:dwarven_mines", "Ancient Dwarven Mines");

    private static final String TAG_PATH = "nearestStructurePath";
    private static final String TAG_NAME = "nearestStructureName";
    private static final String TAG_X = "nearestStructureX";
    private static final String TAG_Z = "nearestStructureZ";
    private static final String TAG_DIMENSION = "nearestStructureDim";
    private static final String TAG_SEARCH = "structureSearch";
    private static final String TAG_NOTIFY = "structureSearchNotify";
    private static final String TAG_RETRY_AT = "structureSearchRetryAt";

    /** How often (in ticks) a compass without a target checks its pending search or starts a new one. */
    private static final int POLL_INTERVAL = 5;
    /** Delay before a failed automatic search is retried in the same dimension (one minute). */
    private static final int RETRY_COOLDOWN = 1200;

    /** Last parsed dimension id; the client asks for it every rendered frame. */
    private static volatile DimensionCache dimensionCache = new DimensionCache(Level.OVERWORLD.location().toString(), Level.OVERWORLD);

    private record DimensionCache(String id, ResourceKey<Level> key) {
    }

    public DwarvenCompass(Properties properties) {
        super(properties);
    }

    public void setStructure(ItemStack stack, StructureName structure) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString(TAG_PATH, structure.getPath());
        tag.putString(TAG_NAME, structure.getName());
    }

    public StructureName getStructure(ItemStack stack) {
        // getTag() instead of getOrCreateTag(): reading must not add an empty tag (it breaks stacking/equality).
        CompoundTag tag = stack.getTag();
        return tag == null ? new StructureName("", "") : new StructureName(tag.getString(TAG_PATH), tag.getString(TAG_NAME));
    }

    public void setStructureX(ItemStack stack, int x) {
        stack.getOrCreateTag().putInt(TAG_X, x);
    }

    public int getStructureX(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag == null ? 0 : tag.getInt(TAG_X);
    }

    public void setStructureZ(ItemStack stack, int z) {
        stack.getOrCreateTag().putInt(TAG_Z, z);
    }

    public int getStructureZ(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag == null ? 0 : tag.getInt(TAG_Z);
    }

    /**
     * Target used by the compass needle, or {@code null} (spinning needle) while nothing is known. The dimension is
     * part of the target, so the needle no longer points to overworld coordinates inside the Nether.
     */
    @Nullable
    public static GlobalPos getTarget(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (!hasTarget(tag)) {
            return null;
        }
        return GlobalPos.of(getDimension(tag), new BlockPos(tag.getInt(TAG_X), 0, tag.getInt(TAG_Z)));
    }

    private static boolean hasTarget(@Nullable CompoundTag tag) {
        return tag != null && tag.contains(TAG_X, Tag.TAG_INT) && tag.contains(TAG_Z, Tag.TAG_INT);
    }

    private static ResourceKey<Level> getDimension(CompoundTag tag) {
        if (!tag.contains(TAG_DIMENSION, Tag.TAG_STRING)) {
            return Level.OVERWORLD; // compasses from older versions only stored overworld positions
        }
        String id = tag.getString(TAG_DIMENSION);
        DimensionCache cache = dimensionCache;
        if (!cache.id().equals(id)) {
            ResourceLocation location = ResourceLocation.tryParse(id);
            if (location == null) {
                return Level.OVERWORLD;
            }
            cache = new DimensionCache(id, ResourceKey.create(Registry.DIMENSION_REGISTRY, location));
            dimensionCache = cache;
        }
        return cache.key();
    }

    private static void clearTarget(CompoundTag tag) {
        tag.remove(TAG_X);
        tag.remove(TAG_Z);
        tag.remove(TAG_DIMENSION);
    }

    private StructureName changeStructure(StructureName structure) {
        return (structure.equals(BROK_AND_SINDRI_SHOP)) ? DWARVEN_MINES : BROK_AND_SINDRI_SHOP;
    }

    @Override
    public boolean isFoil(ItemStack p_40739_) {
        return false;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        // Search bookkeeping lives in NBT; do not bob the item in the player's hand every time it changes.
        return slotChanged || oldStack.getItem() != newStack.getItem();
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        CompoundTag tag = stack.getTag();
        boolean searching = tag != null && tag.contains(TAG_SEARCH, Tag.TAG_LONG);
        if (!searching && hasTarget(tag)) {
            return; // idle compass with a known target: no work, no allocation
        }
        long gameTime = serverLevel.getGameTime();
        if ((gameTime + slot) % POLL_INTERVAL != 0) {
            return;
        }

        if (searching) {
            pollSearch(stack, tag, serverLevel, entity);
            return;
        }
        // No target yet (new compass, or the last search failed): search automatically, but after a failure only
        // retry once the cooldown is over or when the holder changed dimension.
        if (tag != null && tag.contains(TAG_RETRY_AT, Tag.TAG_LONG) && gameTime < tag.getLong(TAG_RETRY_AT)
                && getDimension(tag) == serverLevel.dimension()) {
            return;
        }
        startSearch(stack, serverLevel, entity.blockPosition(), false);
    }

    private void pollSearch(ItemStack stack, CompoundTag tag, ServerLevel level, Entity holder) {
        StructureLocator.Result result = StructureLocator.poll(tag.getLong(TAG_SEARCH));
        switch (result.status()) {
            case PENDING -> {
            }
            // The ticket is gone (server restarted, or the stack was copied and the other copy took the result).
            case UNKNOWN -> startSearch(stack, level, holder.blockPosition(), tag.getBoolean(TAG_NOTIFY));
            case FOUND -> {
                BlockPos pos = result.pos();
                boolean notify = tag.getBoolean(TAG_NOTIFY);
                tag.remove(TAG_SEARCH);
                tag.remove(TAG_NOTIFY);
                tag.remove(TAG_RETRY_AT);
                tag.putInt(TAG_X, pos.getX());
                tag.putInt(TAG_Z, pos.getZ());
                tag.putString(TAG_DIMENSION, result.dimension().location().toString());
                if (notify && holder instanceof Player player) {
                    sendDistance(player, stack);
                }
            }
            case NOT_FOUND -> {
                boolean notify = tag.getBoolean(TAG_NOTIFY);
                tag.remove(TAG_SEARCH);
                tag.remove(TAG_NOTIFY);
                // A failed search in another dimension (e.g. the Nether) must not erase a valid overworld target.
                if (!hasTarget(tag) || getDimension(tag) == result.dimension()) {
                    clearTarget(tag);
                    // Remember where the search failed, so it is retried right away in another dimension.
                    tag.putString(TAG_DIMENSION, result.dimension().location().toString());
                    tag.putLong(TAG_RETRY_AT, level.getGameTime() + RETRY_COOLDOWN);
                }
                if (notify && holder instanceof Player player) {
                    player.sendSystemMessage(Component.literal("Could not find the structure.").withStyle(ChatFormatting.DARK_RED));
                }
            }
        }
    }

    private void startSearch(ItemStack stack, ServerLevel level, BlockPos origin, boolean notify) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(TAG_PATH, Tag.TAG_STRING) || tag.getString(TAG_PATH).isEmpty()) {
            setStructure(stack, BROK_AND_SINDRI_SHOP);
        }
        ResourceLocation structureId = ResourceLocation.tryParse(tag.getString(TAG_PATH));
        tag.putLong(TAG_SEARCH, StructureLocator.submit(level, structureId, origin, StructureLocator.DEFAULT_SEARCH_RADIUS));
        if (notify) {
            tag.putBoolean(TAG_NOTIFY, true);
        } else {
            tag.remove(TAG_NOTIFY);
        }
        tag.remove(TAG_RETRY_AT);
    }

    private void sendDistance(Player player, ItemStack stack) {
        double dx = getStructureX(stack) - player.getBlockX();
        double dz = getStructureZ(stack) - player.getBlockZ();
        long distance = Math.round(Math.sqrt(dx * dx + dz * dz));
        player.sendSystemMessage(Component.literal("The nearest " + getStructure(stack).getName() + " is " + distance + " blocks away."));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level instanceof ServerLevel serverLevel) {
            CompoundTag tag = stack.getTag();
            if (player.isShiftKeyDown()) {
                StructureName changedStructure = changeStructure(getStructure(stack));
                setStructure(stack, changedStructure);
                // The stored position belongs to the previous structure type: drop it so the needle never lies.
                clearTarget(stack.getOrCreateTag());
                player.sendSystemMessage(Component.literal("Looking for " + changedStructure.getName()));
                startSearch(stack, serverLevel, player.blockPosition(), true);
            } else if (tag != null && tag.contains(TAG_SEARCH, Tag.TAG_LONG)) {
                tag.putBoolean(TAG_NOTIFY, true);
                player.sendSystemMessage(Component.literal("Still looking for " + getStructure(stack).getName() + "..."));
            } else if (hasTarget(tag) && getDimension(tag) == serverLevel.dimension()) {
                sendDistance(player, stack);
            } else {
                // No target in this dimension yet: search now and report the result when it arrives.
                startSearch(stack, serverLevel, player.blockPosition(), true);
                player.sendSystemMessage(Component.literal("Looking for " + getStructure(stack).getName()));
            }
        }
        return super.use(level, player, hand);
    }
}
