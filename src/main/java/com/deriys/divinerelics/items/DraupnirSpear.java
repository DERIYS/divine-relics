package com.deriys.divinerelics.items;

import com.deriys.divinerelics.capabilities.stuck_spears.StuckSpearsProvider;
import com.deriys.divinerelics.core.networking.DRMessages;
import com.deriys.divinerelics.core.networking.packets.SpearExplosionParticleS2CPacket;
import com.deriys.divinerelics.core.networking.packets.StuckSpearsS2CPacket;
import com.deriys.divinerelics.entities.entity.ThrownDraupnirSpear;
import com.deriys.divinerelics.init.DRSounds;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.deriys.divinerelics.effects.BifrostProtection.findNormVec;
import static com.deriys.divinerelics.event.DREvents.ForgeEvents.bindItemToEntity;
import static com.deriys.divinerelics.event.DREvents.ForgeEvents.getOwner;

public class DraupnirSpear extends Item {
    public static final int THROW_THRESHOLD_TIME = 8;
    public static final float BASE_DAMAGE = 10.0F;
    public static final float SHOOT_POWER = 3.5F;
    public static final Random RAND = new Random();
    private static final int DELAY_TICKS_THRESHOLD = 1;
    private static final double EXPLOSION_RADIUS = 4.0F;
    private static final float EXPLOSION_DAMAGE = 9.0F;
    private static final int THROWN_SPEARS_THRESHOLD = 5;
    public static final float RANDOM_SOUND_PITCH = RAND.nextFloat() * 0.1F + 0.95F;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public DraupnirSpear(Item.Properties p_43381_) {
        super(p_43381_);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> $$1 = ImmutableMultimap.builder();
        $$1.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", BASE_DAMAGE, AttributeModifier.Operation.ADDITION));
        $$1.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.0000000953674316, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = $$1.build();
    }

    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        return !player.isCreative();
    }

    public UseAnim getUseAnimation(ItemStack p_43417_) {
        return UseAnim.SPEAR;
    }

    public int getThrownCount(ItemStack stack) {
        return stack.getOrCreateTag().getInt("ThrownCount");
    }

    public void setThrownCount(ItemStack stack, int count) {
        stack.getOrCreateTag().putInt("ThrownCount", count);
    }

    public int getDelayTicks(ItemStack stack) {
        return stack.getOrCreateTag().getInt("DelayTicks");
    }

    public void setDelayTicks(ItemStack stack, int count) {
        stack.getOrCreateTag().putInt("DelayTicks", count);
    }

    public boolean getExplosionState(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("ExplosionState");
    }

    public void setExplosionState(ItemStack stack, boolean state) { stack.getOrCreateTag().putBoolean("ExplosionState", state); }

    public void addThrownSpear(ItemStack stack, UUID uuid) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag spearsTag = tag.getList("ThrownSpears", 8);
        spearsTag.add(StringTag.valueOf(uuid.toString()));
        tag.put("ThrownSpears", spearsTag);
    }

    public void setThrownSpears(ItemStack stack, List<UUID> list) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag spearsTag = new ListTag();
        for (UUID uuid: list) {
            spearsTag.add(StringTag.valueOf(uuid.toString()));
        }
        tag.put("ThrownSpears", spearsTag);
    }

    public List<UUID> getThrownSpears(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag spearsTag = tag.getList("ThrownSpears", 8);
        List<UUID> uuids = new ArrayList<>();
        for (int i = 0; i < spearsTag.size(); i++) {
            uuids.add(UUID.fromString(spearsTag.getString(i)));
        }
        return uuids;
    }

    public void resetNBT(ItemStack itemStack) {
        setDelayTicks(itemStack, 0);
        setThrownCount(itemStack, 0);
        setExplosionState(itemStack, false);
    }

    public void clearThrownSpears(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.put("ThrownSpears", new ListTag());
    }

    public boolean isNBTReset(ItemStack stack) {
        return !getExplosionState(stack) && getThrownCount(stack) == 0 && getDelayTicks(stack) == 0 && getThrownSpears(stack).isEmpty();
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);

        int thrownCount = getThrownCount(itemStack);
        int delayTicks = getDelayTicks(itemStack);
        boolean isShiftDown = player.isShiftKeyDown();

        if (thrownCount < THROWN_SPEARS_THRESHOLD && delayTicks == 0 && !isShiftDown){
            player.startUsingItem(interactionHand);
            if (thrownCount == THROWN_SPEARS_THRESHOLD - 1) { setDelayTicks(itemStack, 15); }
            return InteractionResultHolder.pass(itemStack);
        } else if (delayTicks == 0 || isShiftDown){
            BlockPos playerOnPos = player.getOnPos();
            level.playSound(null, playerOnPos, DRSounds.DRAUPNIR_SPEAR_EXPLOSION_HIT.get(), SoundSource.PLAYERS, 2.0F, RAND.nextFloat() * 0.1F + 0.95F);
            setDelayTicks(itemStack, 10);
            setExplosionState(itemStack, true);
            if (!level.isClientSide) {
                Vec2 normVec = findNormVec(player.getLookAngle());
                sendExplosionPacket(level, player.getX() + normVec.x * 0.7, player.getY() - 1, player.getZ() + normVec.y * 0.7, 2D, 0D,25);
            }
            player.getCooldowns().addCooldown(this, 40 * (thrownCount + 1));
            return InteractionResultHolder.consume(itemStack);
        }
        return InteractionResultHolder.fail(itemStack);
    }

    public static void sendExplosionPacket(Level level, double x, double y, double z, double factor, double yRandom, int quantity) {
        SpearExplosionParticleS2CPacket packet = new SpearExplosionParticleS2CPacket(x, y, z, factor, yRandom, quantity);
        DRMessages.sendToChunk(packet, level.getChunkAt(new BlockPos(x, y, z)));
    }

    @Override
    public int getUseDuration(ItemStack p_43419_) {
        return 36000;
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        if (getOwner(itemStack).isEmpty()) {
            bindItemToEntity(entity, itemStack);
        }

        int delayTicks = getDelayTicks(itemStack);
        List<UUID> thrownSpears = getThrownSpears(itemStack);
        if (thrownSpears.isEmpty() && !isNBTReset(itemStack)) {
            resetNBT(itemStack);
        }
        if (delayTicks > DELAY_TICKS_THRESHOLD) {
            setDelayTicks(itemStack, --delayTicks);
        } else if (delayTicks == DELAY_TICKS_THRESHOLD && getExplosionState(itemStack)) {
            UUID uuid = thrownSpears.get(0);
            if (!level.isClientSide) {
                ServerLevel serverLevel = ((ServerLevel) level);
                Entity entityUUID = serverLevel.getEntity(uuid);

                if (isValidSpear(entityUUID) && entity instanceof Player player) {
                    destroySpear(level, entityUUID, player, DamageSource.trident(new ThrownDraupnirSpear(level, player, itemStack), player));
                }
                thrownSpears.remove(0);
                setThrownSpears(itemStack, thrownSpears);
                if (entityUUID != null) {
                    if (entityUUID instanceof ThrownDraupnirSpear) {
                        entityUUID.discard();
                    } else {
                        entityUUID.getCapability(StuckSpearsProvider.STUCK_SPEARS).ifPresent(stuckSpears -> {
                            stuckSpears.removeSpear();
                            DRMessages.sendToAllPlayers(new StuckSpearsS2CPacket(entityUUID.getId(), stuckSpears.getSpears()));
                        });
                        entityUUID.invulnerableTime = 1;
                    }
                    setDelayTicks(itemStack, RAND.nextInt(2, 6));
                }
            }
        } else if (delayTicks == 1 && !getExplosionState(itemStack)){
            setDelayTicks(itemStack, --delayTicks);
        }
        super.inventoryTick(itemStack, level, entity, p_41407_, p_41408_);
    }

    private boolean isValidSpear(Entity entity) {
        return entity instanceof ThrownDraupnirSpear || entity instanceof LivingEntity;
    }

    private void destroySpear(Level level, Entity spear, Player player, DamageSource damageSource) {
        double spearX = spear.getX();
        double spearY = spear.getY();
        double spearZ = spear.getZ();

        List<LivingEntity> entitiesInArea =
                Motosignir.getEntitiesInArea(level, spearX, spearY, spearZ, EXPLOSION_RADIUS);

        Motosignir.hurtAndKnockbackEntites(entitiesInArea, player, spear, damageSource, EXPLOSION_DAMAGE, 0.2f);

        level.playSound(null, spear.getOnPos(), DRSounds.DRAUPNIR_SPEAR_EXPLOSION.get(), SoundSource.PLAYERS, 2.0F, RAND.nextFloat() * 0.1F + 0.95F);

        sendExplosionPacket(level, spearX, spearY, spearZ, 1.5F, 0, 20);
    }

    public static void spawnSpearExplosionParticles(Level level, double x, double y, double z, double factor, double yRandom, int quantity) {
        BlockParticleOption particleOption = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.HONEY_BLOCK.defaultBlockState());
        for (int i = 0; i < quantity; i++) {
            level.addParticle(particleOption,
                    x + (Math.random() - 0.5) * factor,
                    y + (Math.random() - yRandom) * factor,
                    z + (Math.random() - 0.5) * factor,
                    0, 0, 0);
            level.addParticle(ParticleTypes.CRIT,
                    x + (Math.random() - 0.5) * factor,
                    y + (Math.random() - yRandom) * factor,
                    z + (Math.random() - 0.5) * factor,
                    0, 0, 0);
        }
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int timeLeft) {
        if (livingEntity instanceof Player player) {
            int i = this.getUseDuration(itemStack) - timeLeft;
            if (i >= THROW_THRESHOLD_TIME) {
                if (!level.isClientSide) {
                    int thrownCount = getThrownCount(itemStack);
                    ThrownDraupnirSpear thrownSpear = new ThrownDraupnirSpear(level, player, itemStack);
                    thrownSpear.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, SHOOT_POWER, 1.0F);
                    thrownSpear.pickup = AbstractArrow.Pickup.DISALLOWED;

                    thrownSpear.setThrowerPos(player.position());

                    addThrownSpear(itemStack, thrownSpear.getUUID());
                    setThrownCount(itemStack, ++thrownCount);

                    level.addFreshEntity(thrownSpear);
                    level.playSound(null, thrownSpear, DRSounds.DRAUPNIR_SPEAR_THROWING.get(), SoundSource.PLAYERS, 1.0F, RAND.nextFloat() * 0.1F + 0.95F);
                }
            }
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (!slotChanged && oldStack.getItem() == newStack.getItem()) {
            return false;
        }
        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        Vec3 lookVec = player.getLookAngle();
        double length = (entity.position().subtract(player.position())).length();
        double distanceModification = (length > 3F) ? 1.0F: length / 3.5F;
        double speed = 0.7F * distanceModification;
        player.getLevel().playSound(null, player.getOnPos(), DRSounds.DRAUPNIR_SPEAR_HIT.get(), SoundSource.PLAYERS, 1.0f, RAND.nextFloat() * 0.1F + 0.95F);
        player.setDeltaMovement(new Vec3(lookVec.x * speed, 0, lookVec.z * speed));
        return false;
    }

    public boolean hurtEnemy(ItemStack itemStack, LivingEntity p_43391_, LivingEntity attacker) {
        return true;
    }

    public boolean mineBlock(ItemStack itemStack, Level level, BlockState blockState, BlockPos blockPos, LivingEntity livingEntity) {
        if ((double)blockState.getDestroySpeed(level, blockPos) != 0.0) {
            itemStack.hurtAndBreak(2, livingEntity, (p_43385_) -> {
                p_43385_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
        return true;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    public int getEnchantmentValue() {
        return 1;
    }

    @Override
    public boolean isFoil(ItemStack p_41453_) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(Screen.hasShiftDown()) {
            components.add(Component.literal("May this weapon strike true; may it be wielded with wisdom; may it be put down when its job is done."));
        } else {
            components.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.YELLOW));
            String ownerName = stack.getOrCreateTag().getString("OwnerNickname");
            if (!ownerName.isEmpty()) {
                components.add(Component.literal(""));
                components.add(Component.literal("Loyal to: " + ownerName));
            }
        }
    }
}
