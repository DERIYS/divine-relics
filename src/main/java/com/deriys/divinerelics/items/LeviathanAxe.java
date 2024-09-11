package com.deriys.divinerelics.items;

import com.deriys.divinerelics.capabilities.leviathan.LeviathanBindingProvider;
import com.deriys.divinerelics.entities.entity.ThrownLeviathanAxe;
import com.deriys.divinerelics.init.DRSounds;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
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
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.deriys.divinerelics.event.DREvents.ForgeEvents.bindItemToEntity;
import static com.deriys.divinerelics.event.DREvents.ForgeEvents.getOwner;

public class LeviathanAxe extends AxeItem {
    public static final int THROW_THRESHOLD_TIME = 10;
    public static final float BASE_DAMAGE = 19.0F;
    public static final float SHOOT_POWER = 3F;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public LeviathanAxe(Tier p_40521_, float p_40522_, float p_40523_, Properties p_40524_) {
        super(p_40521_, p_40522_, p_40523_, p_40524_);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> $$1 = ImmutableMultimap.builder();
        $$1.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", BASE_DAMAGE, AttributeModifier.Operation.ADDITION));
        $$1.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.4000000953674316, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = $$1.build();
    }

    public boolean canAttackBlock(BlockState p_43409_, Level p_43410_, BlockPos p_43411_, Player p_43412_) {
        return !p_43412_.isCreative();
    }

    public UseAnim getUseAnimation(ItemStack p_43417_) {
        return UseAnim.SPEAR;
    }

    public int getUseDuration(ItemStack p_43419_) {
        return 72000;
    }

    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int use_ticks) {
        if (livingEntity instanceof Player player) {
            int ticks = this.getUseDuration(itemStack) - use_ticks;
            if (ticks >= THROW_THRESHOLD_TIME) {
                if (!level.isClientSide) {
                    ThrownLeviathanAxe thrownLeviathanAxe = new ThrownLeviathanAxe(level, player, itemStack);
                    thrownLeviathanAxe.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, SHOOT_POWER, 1.0F);

                    if (player.getAbilities().instabuild) {
                        thrownLeviathanAxe.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    }

                    level.addFreshEntity(thrownLeviathanAxe);

                    player.getCapability(LeviathanBindingProvider.LEVIATHAN_BINDING).ifPresent(binding -> {
                        binding.addLeviathan(thrownLeviathanAxe.getUUID().toString());
                    });

                    level.playSound(null, player.getOnPos(), DRSounds.LEVIATHAN_AXE_THROW.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

                    if (!player.getAbilities().instabuild) {
                        player.getInventory().removeItem(itemStack);
                    }
                }
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        if (getOwner(itemStack).isEmpty()) {
            bindItemToEntity(entity, itemStack);
        }
        super.inventoryTick(itemStack, level, entity, p_41407_, p_41408_);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (entity instanceof LivingEntity livingEntity && !player.getLevel().isClientSide) {
            livingEntity.setTicksFrozen(livingEntity.getTicksFrozen() + 60);
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    public boolean hurtEnemy(ItemStack p_43390_, LivingEntity p_43391_, LivingEntity p_43392_) {
        return true;
    }

    public boolean mineBlock(ItemStack p_43399_, Level p_43400_, BlockState p_43401_, BlockPos p_43402_, LivingEntity p_43403_) {
        return true;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot p_43383_) {
        return p_43383_ == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(p_43383_);
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
        if (Screen.hasShiftDown()) {
            components.add(Component.literal("Once wielded by Kratos’ wife, this axe embodies icy vengeance. Its enchanted blade cleaves through foes, always returning to its wielder’s grasp."));
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
