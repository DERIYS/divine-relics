package com.deriys.divinerelics.items;

import com.deriys.divinerelics.util.custom.StructureName;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.Collections;

public class DwarvenCompass extends Item {
    public final static StructureName BROK_AND_SINDRI_SHOP = new StructureName("divinerelics:brok_and_sindri_shop", "Brok and Sindri Shop");
    public final static StructureName DWARVEN_MINES = new StructureName("divinerelics:dwarven_mines", "Ancient Dwarven Mines");

    public DwarvenCompass(Properties properties) {
        super(properties);
    }

    public void setStructure(ItemStack stack, StructureName structure) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString("nearestStructurePath", structure.getPath());
        tag.putString("nearestStructureName", structure.getName());
    }

    public StructureName getStructure(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        String path = tag.getString("nearestStructurePath");
        String name = tag.getString("nearestStructureName");
        return new StructureName(path, name);
    }

    public void setStructureX(ItemStack stack, int x) {
        stack.getOrCreateTag().putInt("nearestStructureX", x);
    }

    public int getStructureX(ItemStack stack) {
        return stack.getOrCreateTag().getInt("nearestStructureX");
    }

    public void setStructureZ(ItemStack stack, int z) {
        stack.getOrCreateTag().putInt("nearestStructureZ", z);
    }

    public int getStructureZ(ItemStack stack) {
        return stack.getOrCreateTag().getInt("nearestStructureZ");
    }

    private StructureName changeStructure(StructureName structure) {
        return (structure.equals(BROK_AND_SINDRI_SHOP)) ? DWARVEN_MINES : BROK_AND_SINDRI_SHOP;
    }

    @Override
    public boolean isFoil(ItemStack p_40739_) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (!level.isClientSide) {
            boolean containsPos = stack.getOrCreateTag().contains("nearestStructureX");
            if (!containsPos) {
                BlockPos shopPos = findNearestStructure(((ServerLevel) level), entity.getOnPos(), BROK_AND_SINDRI_SHOP.getPath());
                setStructure(stack, BROK_AND_SINDRI_SHOP);
                if (shopPos != null) {
                    setStructureX(stack, shopPos.getX());
                    setStructureZ(stack, shopPos.getZ());
                }
            }
        }
    }

    public static BlockPos findNearestStructure(ServerLevel level, BlockPos pos, String structure) {
        ResourceKey<Structure> structureKey = ResourceKey.create(Registry.STRUCTURE_REGISTRY, new ResourceLocation(structure));
        Holder<Structure> structureHolder = level.registryAccess().registryOrThrow(Registry.STRUCTURE_REGISTRY).getHolderOrThrow(structureKey);

        HolderSet<Structure> holderset = HolderSet.direct(Collections.singletonList(structureHolder));
        Pair<BlockPos, Holder<Structure>> result = level.getChunkSource().getGenerator().findNearestMapStructure(level, holderset, pos, 100, false);
        if (result != null) {
            return result.getFirst();
        }
        return null;
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            boolean empty = false;
            ItemStack stack = player.getItemInHand(hand);

            BlockPos playerOnPos = player.getOnPos();
            if (player.isShiftKeyDown()) {
                StructureName structure = this.getStructure(stack);
                if (structure.getPath() != null) {
                    StructureName changedStructure = changeStructure(structure);
                    this.setStructure(stack, changedStructure);

                    player.sendSystemMessage(Component.literal("Looking for " + changedStructure.getName()));
                    BlockPos shopPos = findNearestStructure(((ServerLevel) level), playerOnPos, changedStructure.getPath());

                    if (shopPos != null) {
                        setStructureX(stack, shopPos.getX());
                        setStructureZ(stack, shopPos.getZ());
                    } else {
                        player.sendSystemMessage(Component.literal("Could not find the structure.").withStyle(ChatFormatting.DARK_RED));
                    }
                } else {
                    empty = true;
                }
            }
            if (!empty) {
                StructureName structure = this.getStructure(stack);
                int shopPosX = this.getStructureX(stack);
                int shopPosZ = this.getStructureZ(stack);
                player.sendSystemMessage(Component.literal("The nearest " + structure.getName() + " is " + Math.round(Math.sqrt(playerOnPos.distSqr(new BlockPos(shopPosX, playerOnPos.getY(), shopPosZ)))) + " blocks away."));
            }
        }
        return super.use(level, player, hand);
    }
}