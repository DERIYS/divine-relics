package com.deriys.divinerelics.items;

import com.deriys.divinerelics.capabilities.mjolnir.MjolnirBindingProvider;
import com.deriys.divinerelics.config.DivineRelicsCommonConfig;
import com.deriys.divinerelics.entities.entity.ThrownMjolnir;
import com.deriys.divinerelics.init.DRSounds;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.deriys.divinerelics.event.DREvents.ForgeEvents.bindItemToEntity;
import static com.deriys.divinerelics.event.DREvents.ForgeEvents.getOwner;

public class Mjolnir extends AxeItem {
    public static final int THROW_THRESHOLD_TIME = DivineRelicsCommonConfig.MJOLNIR_THROW_THRESHOLD.get();
    public static final double BASE_DAMAGE = DivineRelicsCommonConfig.MJOLNIR_DAMAGE.get();
    public static final double BASE_ATTACK_SPEED = DivineRelicsCommonConfig.MJOLNIR_ATTACK_SPEED.get();
    public static final double SHOOT_POWER = DivineRelicsCommonConfig.MJOLNIR_SHOOT_POWER.get();
    private static final int RIPTIDE_COOLDOWN = DivineRelicsCommonConfig.MJOLNIR_RIPTIDE_COOLDOWN.get();
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public Mjolnir(Tier p_40521_, float p_40522_, float p_40523_, Properties p_40524_) {
        super(p_40521_, p_40522_, p_40523_, p_40524_);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> $$1 = ImmutableMultimap.builder();
        $$1.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", BASE_DAMAGE, AttributeModifier.Operation.ADDITION));
        $$1.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", BASE_ATTACK_SPEED, AttributeModifier.Operation.ADDITION));
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
                if (!player.isShiftKeyDown() && !level.isClientSide) {
                    // Adding thrown Mjölnir
                    ThrownMjolnir thrownMjolnir = new ThrownMjolnir(level, player, itemStack);
                    thrownMjolnir.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, (float) SHOOT_POWER, 1.0F);
                    if (player.getAbilities().instabuild) {
                        thrownMjolnir.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    }
                    thrownMjolnir.setNoGravity(true);
                    level.addFreshEntity(thrownMjolnir);

                    // Binding thrown Mjölnir to the player
                    player.getCapability(MjolnirBindingProvider.MJOLNIR_BINDING).ifPresent(binding -> {
                        binding.addMjolnir(thrownMjolnir.getUUID().toString());
                    });

                    level.playSound(null, player.getOnPos(), DRSounds.MJOLNIR_THROWING.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                    
                    if (!player.getAbilities().instabuild) {
                        player.getInventory().removeItem(itemStack);
                    }

                } else if (player.isShiftKeyDown() && !player.isFallFlying()) { // riptide
                    player.getCooldowns().addCooldown(itemStack.getItem(), RIPTIDE_COOLDOWN);
                    if (!isRiptideFlying(itemStack)) {
                        setRiptideFlying(itemStack, true);
                    }
                    float f7 = player.getYRot();
                    float f = player.getXRot();
                    float f1 = -Mth.sin(f7 * ((float) Math.PI / 180F)) * Mth.cos(f * ((float) Math.PI / 180F));
                    float f2 = -Mth.sin(f * ((float) Math.PI / 180F));
                    float f3 = Mth.cos(f7 * ((float) Math.PI / 180F)) * Mth.cos(f * ((float) Math.PI / 180F));
                    float f4 = Mth.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
                    float f5 = 6.0F;
                    f1 *= f5 / f4;
                    f2 *= f5 / f4;
                    f3 *= f5 / f4;
                    player.push(f1, f2, f3);
                    player.startAutoSpinAttack(30);
                    if (player.isOnGround()) {
                        float f6 = 1.1999999F;
                        player.move(MoverType.SELF, new Vec3(0.0D, f6, 0.0D));
                    }

                    level.playSound((Player) null, player, DRSounds.MJOLNIR_RIPTIDE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                }
                player.awardStat(Stats.ITEM_USED.get(this));
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

    public void setRiptideFlying(ItemStack mjolnir, boolean isFlying) {
        if (mjolnir != null) {
            CompoundTag nbt = mjolnir.getOrCreateTag();
            nbt.putBoolean("RiptideFlying", isFlying);
        }
    }

    public boolean isRiptideFlying(ItemStack mjolnir) {
        if (mjolnir != null) {
            CompoundTag nbt = mjolnir.getOrCreateTag();
            return nbt.getBoolean("RiptideFlying");
        }
        return false;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemStack);
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
            components.add(Component.literal("The legendary hammer of the God of Thunder, forged by the Huldra brothers, is now at your possession. Be careful, for the hammer does not make you its original possessor."));
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
