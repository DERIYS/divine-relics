package com.deriys.divinerelics.items;

import com.deriys.divinerelics.entities.ThrownMjolnir;
import com.deriys.divinerelics.sound.DRSounds;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
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

public class Mjolnir extends Item implements Vanishable {
    public static final int THROW_THRESHOLD_TIME = 10;
    public static final float BASE_DAMAGE = 20.0F;
    public static final float SHOOT_POWER = 3F;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public Mjolnir(Item.Properties properties) {
        super(properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> $$1 = ImmutableMultimap.builder();
        $$1.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", BASE_DAMAGE, AttributeModifier.Operation.ADDITION));
        $$1.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -3.2500000953674316, AttributeModifier.Operation.ADDITION));
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
            if (ticks >= 10) {
                if (!level.isClientSide) {
                    if (!player.isShiftKeyDown()) {
                        ThrownMjolnir thrownMjolnir = new ThrownMjolnir(level, player, itemStack);
                        thrownMjolnir.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, SHOOT_POWER, 1.0F);
                        if (player.getAbilities().instabuild) {
                            thrownMjolnir.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        }

                        level.addFreshEntity(thrownMjolnir);
                        level.playSound((Player)null, thrownMjolnir, DRSounds.MJOLNIR_THROWING.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                        if (!player.getAbilities().instabuild) {
                            player.getInventory().removeItem(itemStack);
                        }
                    }
                    player.awardStat(Stats.ITEM_USED.get(this));
//                    if ($$6 > 0) {
//                        float $$8 = player.getYRot();
//                        float $$9 = player.getXRot();
//                        float $$10 = -Mth.sin($$8 * 0.017453292F) * Mth.cos($$9 * 0.017453292F);
//                        float $$11 = -Mth.sin($$9 * 0.017453292F);
//                        float $$12 = Mth.cos($$8 * 0.017453292F) * Mth.cos($$9 * 0.017453292F);
//                        float $$13 = Mth.sqrt($$10 * $$10 + $$11 * $$11 + $$12 * $$12);
//                        float $$14 = 3.0F * ((1.0F + (float)$$6) / 4.0F);
//                        $$10 *= $$14 / $$13;
//                        $$11 *= $$14 / $$13;
//                        $$12 *= $$14 / $$13;
//                        player.push((double)$$10, (double)$$11, (double)$$12);
//                        player.startAutoSpinAttack(20);
//                        if (player.isOnGround()) {
//                            float $$15 = 1.1999999F;
//                            player.move(MoverType.SELF, new Vec3(0.0, 1.1999999284744263, 0.0));
//                        }
//
//                        SoundEvent $$18;
//                        if ($$6 >= 3) {
//                            $$18 = SoundEvents.TRIDENT_RIPTIDE_3;
//                        } else if ($$6 == 2) {
//                            $$18 = SoundEvents.TRIDENT_RIPTIDE_2;
//                        } else {
//                            $$18 = SoundEvents.TRIDENT_RIPTIDE_1;
//                        }
//
//                        level.playSound((Player)null, player, $$18, SoundSource.PLAYERS, 1.0F, 1.0F);
//                    }

                }
            }
        }
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
}
