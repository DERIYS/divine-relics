package com.deriys.tutorialmod.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Spear extends TridentItem {

    private static final int EXPLOSION_RADIUS = 2;

    public Spear(Properties properties) {
        super(properties.stacksTo(1));
    }

    private int getThrownCount(ItemStack stack) {
        return stack.getOrCreateTag().getInt("ThrownCount");
    }

    private void setThrownCount(ItemStack stack, int count) {
        stack.getOrCreateTag().putInt("ThrownCount", count);
    }

    private int getDelayTicks(ItemStack stack) {
        return stack.getOrCreateTag().getInt("DelayTicks");
    }

    private void setDelayTicks(ItemStack stack, int count) {
        stack.getOrCreateTag().putInt("DelayTicks", count);
    }

    private void addThrownSpear(ItemStack stack, UUID uuid) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag spearsTag = tag.getList("ThrownSpears", 8);  // 8 is the NBT type for StringTag
        spearsTag.add(StringTag.valueOf(uuid.toString()));
        tag.put("ThrownSpears", spearsTag);
    }

    private List<UUID> getThrownSpears(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag spearsTag = tag.getList("ThrownSpears", 8);
        List<UUID> uuids = new ArrayList<>();
        for (int i = 0; i < spearsTag.size(); i++) {
            uuids.add(UUID.fromString(spearsTag.getString(i)));
        }
        return uuids;
    }

    private void clearThrownSpears(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.put("ThrownSpears", new ListTag());
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);

        int thrownCount = getThrownCount(itemStack);
        int delayTicks = getDelayTicks(itemStack);

        if (thrownCount < 3 && delayTicks == 0){
            player.startUsingItem(interactionHand);
            if (thrownCount == 2) { setDelayTicks(itemStack, 20); }
            return InteractionResultHolder.pass(itemStack);
        } else if (delayTicks == 0){
            if (!level.isClientSide) {
                ServerLevel serverLevel = (ServerLevel) level;
                for (UUID uuid : getThrownSpears(itemStack)) {
                    Entity entity = serverLevel.getEntity(uuid);
                    if (entity instanceof ThrownTrident) {
                        entity.discard();
                    }
                }
            }
            setThrownCount(itemStack, 0);
            setDelayTicks(itemStack, 20);
            clearThrownSpears(itemStack);
            return InteractionResultHolder.consume(itemStack);
        }
        return InteractionResultHolder.fail(itemStack);
    }

    @Override
    public int getUseDuration(ItemStack p_43419_) {
        return 36000;
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        int delayTicks = getDelayTicks(itemStack);
        if (delayTicks > 0) {
            setDelayTicks(itemStack, --delayTicks);
        }
        super.inventoryTick(itemStack, level, entity, p_41407_, p_41408_);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int timeLeft) {
        if (livingEntity instanceof Player player) {
            int i = this.getUseDuration(itemStack) - timeLeft;
            if (i >= 5) {
                if (!level.isClientSide) {
                    itemStack.hurtAndBreak(1, player, (player1) -> {
                        player1.broadcastBreakEvent(livingEntity.getUsedItemHand());
                    });

                    int thrownCount = getThrownCount(itemStack);
                    ThrownTrident thrownTrident = new ThrownTrident(level, player, itemStack);
                    thrownTrident.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.5F, 1.0F);
                    thrownTrident.pickup = AbstractArrow.Pickup.DISALLOWED;
                    addThrownSpear(itemStack, thrownTrident.getUUID());
                    setThrownCount(itemStack, ++thrownCount);

                    level.addFreshEntity(thrownTrident);
                    level.playSound(null, thrownTrident, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            }
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_43417_) {
        return super.getUseAnimation(p_43417_);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (!slotChanged && oldStack.getItem() == newStack.getItem()) {
            return false;
        }

        // Otherwise, use default behavior
        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
    }
}
