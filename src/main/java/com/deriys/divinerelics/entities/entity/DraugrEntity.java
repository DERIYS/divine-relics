package com.deriys.divinerelics.entities.entity;

import com.deriys.divinerelics.config.DivineRelicsCommonConfig;
import com.deriys.divinerelics.entities.ai.TimedAttack;
import com.deriys.divinerelics.entities.ai.draugr.DraugrAttackGoal;
import com.deriys.divinerelics.entities.client.SyncedAnimationController;
import com.deriys.divinerelics.init.DRItems;
import com.deriys.divinerelics.init.DRSounds;
import com.deriys.divinerelics.items.DraupnirSpear;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import static com.deriys.divinerelics.items.HeimdallGauntlet.RAND;

public class DraugrEntity extends Monster implements IAnimatable {
    private static final AttributeModifier SLOW_SPEED_MODIFIER = new AttributeModifier("SlowSpeed", -0.3, AttributeModifier.Operation.MULTIPLY_BASE);
    private static final AttributeModifier FAST_SPEED_MODIFIER = new AttributeModifier("FastSpeed", DivineRelicsCommonConfig.DRAUGR_SPEED_MODIFIER.get(), AttributeModifier.Operation.MULTIPLY_BASE);

    public static final int BURNING_TIME = DivineRelicsCommonConfig.DRAUGR_BURNING_TIME.get();
    /** The attack lasts as long as its animation (animation.draugr.attack) and hits halfway through it. */
    public static final int ATTACK_DURATION = 30;
    public static final int ATTACK_IMPACT_TICK = 15;
    /** Extra reach of the attack on top of the usual melee reach. */
    public static final float ATTACK_RANGE = 2.8F;
    private static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(DraugrEntity.class, EntityDataSerializers.BOOLEAN);
    /** Game time at which the current attack started on the server: clients play its animation from there. */
    private static final EntityDataAccessor<Integer> ATTACK_START_TIME =
            SynchedEntityData.defineId(DraugrEntity.class, EntityDataSerializers.INT);
    /** Server: the current attack's progress (see {@link #startAttack}). */
    private final TimedAttack attack = new TimedAttack();
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private SoundEvent[] ambientSounds = {
            DRSounds.DRAUGR_AMBIENT_1.get(),
            DRSounds.DRAUGR_AMBIENT_2.get(),
            DRSounds.DRAUGR_AMBIENT_3.get(),
    };

    public DraugrEntity(EntityType<? extends DraugrEntity> entityType, Level level) {
        super(entityType, level);
    }

    public void setAttacking(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    /**
     * Starts an attack on {@code target}. It plays out to the end on its own (see {@link #tick}), whether or not the
     * Draugr still has a target by then: its animation and its hit always stay together.
     */
    public void startAttack(@Nullable LivingEntity target) {
        this.setAttacking(true);
        this.entityData.set(ATTACK_START_TIME, (int) this.level.getGameTime());
        this.attack.start(target);
        this.level.playSound(null, this.getOnPos(), DRSounds.DRAUGR_ATTACK.get(), SoundSource.HOSTILE, 1.0f, RAND.nextFloat() * 0.1F + 0.95F);
    }

    private void performAttackImpact() {
        this.swing(InteractionHand.MAIN_HAND);
        // The swing always happens; it only lands on a target that is still alive and within reach.
        LivingEntity target = this.attack.getTarget();
        if (target != null && this.attack.targetInReach(this, TimedAttack.meleeRangeSqr(this, target, ATTACK_RANGE))
                && this.doHurtTarget(target) && target.isAlive()
                && target instanceof Player player && !player.getAbilities().instabuild) {
            target.setRemainingFireTicks(target.getRemainingFireTicks() + BURNING_TIME);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING, false);
        this.entityData.define(ATTACK_START_TIME, 0);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level.isClientSide && this.isAlive()
                && this.attack.tick(this, ATTACK_IMPACT_TICK, ATTACK_DURATION, this::performAttackImpact)) {
            this.setAttacking(false);
        }

        LivingEntity target = this.getTarget();
        setChasing(this, FAST_SPEED_MODIFIER, SLOW_SPEED_MODIFIER, target != null && target.isAlive());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.addBehaviourGoals();
    }

    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(1, new DraugrAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(DraugrEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Villager.class, true));
        this.goalSelector.addGoal(4, new FloatGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, DivineRelicsCommonConfig.DRAUGR_FOLLOW_RANGE.get())
                .add(Attributes.MAX_HEALTH, DivineRelicsCommonConfig.DRAUGR_HP.get())
                .add(Attributes.KNOCKBACK_RESISTANCE, DivineRelicsCommonConfig.DRAUGR_KB_RESISTANCE.get())
                .add(Attributes.MOVEMENT_SPEED, 0.22F)
                .add(Attributes.ATTACK_DAMAGE, DivineRelicsCommonConfig.DRAUGR_DAMAGE.get())
                .add(Attributes.ARMOR, DivineRelicsCommonConfig.DRAUGR_ARMOR.get())
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.isAttacking()) {
            // Played from where the server is in the attack, also if it started while the Draugr was not drawn.
            int startTime = this.entityData.get(ATTACK_START_TIME);
            double elapsed = SyncedAnimationController.ticksSince(this.level, startTime, event.getPartialTick());
            if (elapsed < ATTACK_DURATION) {
                if (event.getController() instanceof SyncedAnimationController<?> controller) {
                    controller.playSynced("animation.draugr.attack", startTime, elapsed);
                }
                return PlayState.CONTINUE;
            }
        }

        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.draugr.walk", ILoopType.EDefaultLoopTypes.LOOP));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.draugr.idle", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }

    public static void setChasing(LivingEntity livingEntity, AttributeModifier fast_speed_modifier, AttributeModifier slow_speed_modifier, boolean chasing) {
        AttributeInstance speedAttribute = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speedAttribute != null) {
            if (chasing) {
                if (!speedAttribute.hasModifier(fast_speed_modifier)) {
                    speedAttribute.addTransientModifier(fast_speed_modifier);
                }
                if (speedAttribute.hasModifier(slow_speed_modifier)) {
                    speedAttribute.removeModifier(slow_speed_modifier);
                }
            } else {
                if (!speedAttribute.hasModifier(slow_speed_modifier)) {
                    speedAttribute.addTransientModifier(slow_speed_modifier);
                }
                if (speedAttribute.hasModifier(fast_speed_modifier)) {
                    speedAttribute.removeModifier(fast_speed_modifier);
                }
            }
        }
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new SyncedAnimationController<>(this, "controller",
                0, this::predicate));
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundEvents.SKELETON_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.ambientSounds[RAND.nextInt(0, 3)];

    }

    @Override
    public float getVoicePitch() {
        return DraupnirSpear.getRandomPitch();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    @Override
    public void die(DamageSource p_21014_) {
        if (RAND.nextFloat() > 0.5f) {
            this.spawnAtLocation(new ItemStack(DRItems.HACKSILVER.get(), RAND.nextInt(0, 2)));
        }
        if (RAND.nextFloat() < 0.01f) {
            this.spawnAtLocation(new ItemStack(DRItems.SCAPE_THEME_MUSIC_DISK.get()));
        }
        super.die(p_21014_);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public static boolean canSpawn(EntityType<DraugrEntity> draugrEntityEntityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        return checkMobSpawnRules(draugrEntityEntityType, serverLevelAccessor, mobSpawnType, blockPos, randomSource) && serverLevelAccessor.getDifficulty() != Difficulty.PEACEFUL;
    }
}
