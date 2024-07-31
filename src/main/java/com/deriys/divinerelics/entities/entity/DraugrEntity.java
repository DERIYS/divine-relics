package com.deriys.divinerelics.entities.entity;

import com.deriys.divinerelics.entities.ai.draugr.DraugrAttackGoal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class DraugrEntity extends Monster implements IAnimatable {
    private static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(DraugrEntity.class, EntityDataSerializers.BOOLEAN);
    public int attackingTicks = 0;

    public enum AnimationState {
        IDLE,
        WALKING,
        ATTACKING
    }
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private AnimationState currentState = AnimationState.IDLE;

    public DraugrEntity(EntityType<? extends DraugrEntity> entityType, Level level) {
        super(entityType, level);
    }

    public void setAttacking(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING, false);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isAttacking()) {
            this.currentState = AnimationState.ATTACKING;
        }

        if (this.level.isClientSide && this.attackingTicks > 0){
            this.attackingTicks--;
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.addBehaviourGoals();
    }

    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(2, new DraugrAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(DraugrEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Villager.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 35.0D).add(Attributes.MOVEMENT_SPEED, (double)0.28F).add(Attributes.ATTACK_DAMAGE, 5.0D).add(Attributes.ARMOR, 7.0D).add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
    }

    public static DraugrEntity create(EntityType<? extends DraugrEntity> entityType, Level level) {
        return new DraugrEntity(entityType, level);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(this.isAttacking() && this.attackingTicks == 0) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.draugr.attack", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            this.attackingTicks = 20;
            return PlayState.CONTINUE;
        }

        if (this.attackingTicks == 0) {
            if (event.isMoving()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.draugr.walk", ILoopType.EDefaultLoopTypes.LOOP));
                return PlayState.CONTINUE;
            }
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.draugr.idle", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }

//    private <E extends IAnimatable> PlayState attackPredicate(AnimationEvent<E> event) {
//        if(this.swinging) {
//            event.getController().markNeedsReload();
//            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.draugr.attack", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
//            System.out.println("ATTACK");
//            this.attackingTicks = 20;
//            this.swinging = false;
//        }
//        return PlayState.CONTINUE;
//    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
//        animationData.addAnimationController(new AnimationController(this, "attackController",
//                0, this::attackPredicate));
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundEvents.SKELETON_HURT;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
