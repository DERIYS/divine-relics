package com.deriys.divinerelics.entities.entity;

import com.deriys.divinerelics.dwarfs.DRDwarfs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class SindriEntity extends Villager implements IAnimatable {
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private long lastRestockTime = 0;
    private static final int RESTOCK_INTERVAL = 600;

    public SindriEntity(EntityType<? extends Villager> p_35381_, Level p_35382_) {
        super(p_35381_, p_35382_);
        this.setVillagerData(this.getVillagerData().setProfession(DRDwarfs.SINDRI.get()));
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        this.setVillagerData(this.getVillagerData().setProfession(DRDwarfs.SINDRI.get()));
    }

    @Override
    public void setVillagerData(VillagerData villagerData) {
        if (villagerData.getProfession() != DRDwarfs.SINDRI.get()) {
            super.setVillagerData(villagerData.setProfession(DRDwarfs.SINDRI.get()));
        } else {
            super.setVillagerData(villagerData);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            long currentTime = this.level.getGameTime();
            if (this.needsRestock() && currentTime - lastRestockTime >= RESTOCK_INTERVAL) {
                restockTrades();
                lastRestockTime = currentTime;
            }
        }
    }

    @Override
    public void thunderHit(ServerLevel p_35409_, LightningBolt p_35410_) {

    }

    private void restockTrades() {
        if (this.offers != null) {
            for (MerchantOffer offer : this.offers) {
                offer.resetUses();
            }
        }
    }

    private boolean needsRestock() {
        if (this.offers != null) {
            for (MerchantOffer offer : this.offers) {
                if (offer.needsRestock()) {
                    return true;
                }
            }
        } return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putLong("LastRestockTime", lastRestockTime);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("LastRestockTime")) {
            lastRestockTime = compound.getLong("LastRestockTime");
        }
    }

    @Override
    public MerchantOffers getOffers() {
        MerchantOffers offers = super.getOffers();
        for (MerchantOffer offer : offers) {
            offer.resetSpecialPriceDiff();
        }
        return offers;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(SindriEntity.class));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.addBehaviourGoals();
    }

    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    }

    @Override
    public boolean hurt(DamageSource source, float p_21017_) {
        if (!(source.getEntity() instanceof Player)) {
            super.hurt(source, p_21017_);
        } return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0)
                .add(Attributes.FOLLOW_RANGE, 5.0D)
                .add(Attributes.MAX_HEALTH, 1000D)
                .add(Attributes.ATTACK_DAMAGE, 10D);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.sindri.walk", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.sindri.idle", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_35498_) {
        return SoundEvents.GENERIC_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.GENERIC_DEATH;
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return null;
    }

    @Override
    protected SoundEvent getTradeUpdatedSound(boolean p_35323_) {
        return null;
    }
}
