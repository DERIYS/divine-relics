package com.deriys.divinerelics.entities.entity;

import com.deriys.divinerelics.config.DivineRelicsCommonConfig;
import com.deriys.divinerelics.entities.ai.TimedAttack;
import com.deriys.divinerelics.entities.ai.hel_walker.HelWalkerAttackGoal;
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
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
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

import static com.deriys.divinerelics.entities.entity.DraugrEntity.setChasing;
import static com.deriys.divinerelics.items.HeimdallGauntlet.RAND;

public class HelWalkerEntity extends Monster implements IAnimatable {
    private static final AttributeModifier SLOW_SPEED_MODIFIER = new AttributeModifier("SlowSpeed", -0.3, AttributeModifier.Operation.MULTIPLY_BASE);
    private static final AttributeModifier FAST_SPEED_MODIFIER = new AttributeModifier("FastSpeed", DivineRelicsCommonConfig.HEL_WALKER_SPEED_MODIFIER.get(), AttributeModifier.Operation.MULTIPLY_BASE);
    public static final int FREEZING_TIME = DivineRelicsCommonConfig.HEL_WALKER_FREEZING_TIME.get();
    /** The attack lasts as long as its animations (animation.hel_walker.attack_1/2) and hits halfway through them. */
    public static final int ATTACK_DURATION = 30;
    public static final int ATTACK_IMPACT_TICK = 15;
    /** Extra reach of the attack on top of the usual melee reach. */
    public static final float ATTACK_RANGE = 2.8F;
    private static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(HelWalkerEntity.class, EntityDataSerializers.BOOLEAN);
    /** Game time at which the current attack started on the server: clients play its animation from there. */
    private static final EntityDataAccessor<Integer> ATTACK_START_TIME =
            SynchedEntityData.defineId(HelWalkerEntity.class, EntityDataSerializers.INT);
    /** Which of {@link #helWalkerAttacks} the current attack plays, picked by the server so every client sees the same. */
    private static final EntityDataAccessor<Byte> ATTACK_ANIMATION =
            SynchedEntityData.defineId(HelWalkerEntity.class, EntityDataSerializers.BYTE);
    /** Server: the current attack's progress (see {@link #startAttack}). */
    private final TimedAttack attack = new TimedAttack();
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private final SoundEvent[] attackSounds = {
            DRSounds.HEL_WALKER_ATTACK_1.get(),
            DRSounds.HEL_WALKER_ATTACK_2.get(),
            DRSounds.HEL_WALKER_ATTACK_3.get(),
    };

    private final SoundEvent[] ambientSounds = {
            DRSounds.HEL_WALKER_AMBIENT_1.get(),
            DRSounds.HEL_WALKER_AMBIENT_2.get(),
            DRSounds.HEL_WALKER_AMBIENT_3.get(),
            DRSounds.HEL_WALKER_AMBIENT_4.get(),
            DRSounds.HEL_WALKER_AMBIENT_5.get(),
            DRSounds.HEL_WALKER_AMBIENT_6.get(),
    };

    private final SoundEvent[] hitSounds = {
            DRSounds.HEL_WALKER_HIT_1.get(),
            DRSounds.HEL_WALKER_HIT_2.get(),
            DRSounds.HEL_WALKER_HIT_3.get(),
            DRSounds.HEL_WALKER_HIT_4.get(),
            DRSounds.HEL_WALKER_HIT_5.get()
    };

    private final String[] helWalkerAttacks = {
            "animation.hel_walker.attack_1",
            "animation.hel_walker.attack_2"
    };

    public HelWalkerEntity(EntityType<? extends HelWalkerEntity> entityType, Level level) {
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
     * Hel Walker still has a target by then: its animation and its hit always stay together.
     */
    public void startAttack(@Nullable LivingEntity target) {
        this.setAttacking(true);
        this.entityData.set(ATTACK_START_TIME, (int) this.level.getGameTime());
        this.entityData.set(ATTACK_ANIMATION, (byte) RAND.nextInt(this.helWalkerAttacks.length));
        this.attack.start(target);
        this.level.playSound(null, this.getOnPos(), this.getAttackSound(), SoundSource.HOSTILE, 1.0f, RAND.nextFloat() * 0.1F + 0.95F);
    }

    private void performAttackImpact() {
        this.swing(InteractionHand.MAIN_HAND);
        // The swing always happens; it only lands on a target that is still alive and within reach.
        LivingEntity target = this.attack.getTarget();
        if (target != null && this.attack.targetInReach(this, TimedAttack.meleeRangeSqr(this, target, ATTACK_RANGE))
                && this.doHurtTarget(target) && target.isAlive()
                && target instanceof Player player && !player.getAbilities().instabuild) {
            target.setTicksFrozen(target.getTicksFrozen() + FREEZING_TIME);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING, false);
        this.entityData.define(ATTACK_START_TIME, 0);
        this.entityData.define(ATTACK_ANIMATION, (byte) 0);
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
        this.goalSelector.addGoal(1, new HelWalkerAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(HelWalkerEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Villager.class, true));
        this.goalSelector.addGoal(4, new FloatGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, DivineRelicsCommonConfig.HEL_WALKER_FOLLOW_RANGE.get())
                .add(Attributes.MAX_HEALTH, DivineRelicsCommonConfig.HEL_WALKER_HP.get())
                .add(Attributes.KNOCKBACK_RESISTANCE, DivineRelicsCommonConfig.HEL_WALKER_KB_RESISTANCE.get())
                .add(Attributes.MOVEMENT_SPEED, 0.22F)
                .add(Attributes.ATTACK_DAMAGE, DivineRelicsCommonConfig.HEL_WALKER_DAMAGE.get())
                .add(Attributes.ARMOR, DivineRelicsCommonConfig.HEL_WALKER_ARMOR.get())
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.isAttacking()) {
            // Played from where the server is in the attack, also if it started while the Hel Walker was not drawn.
            int startTime = this.entityData.get(ATTACK_START_TIME);
            double elapsed = SyncedAnimationController.ticksSince(this.level, startTime, event.getPartialTick());
            if (elapsed < ATTACK_DURATION) {
                if (event.getController() instanceof SyncedAnimationController<?> controller) {
                    int variant = Mth.clamp(this.entityData.get(ATTACK_ANIMATION), 0, this.helWalkerAttacks.length - 1);
                    controller.playSynced(this.helWalkerAttacks[variant], startTime, elapsed);
                }
                return PlayState.CONTINUE;
            }
        }

        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.hel_walker.walk", ILoopType.EDefaultLoopTypes.LOOP));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.hel_walker.idle", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new SyncedAnimationController<>(this, "controller",
                0, this::predicate));
    }

    public SoundEvent getAttackSound() {
        return this.attackSounds[RAND.nextInt(this.attackSounds.length)];
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return this.hitSounds[RAND.nextInt(this.hitSounds.length)]; // was nextInt(0, 3): hit sounds 4 and 5 never played
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.ambientSounds[RAND.nextInt(this.ambientSounds.length)];
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.STRAY_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return DraupnirSpear.getRandomPitch();
    }

    @Override
    public void die(DamageSource damageSource) {
        if (RAND.nextFloat() > 0.5f) {
            this.spawnAtLocation(new ItemStack(DRItems.HACKSILVER.get(), RAND.nextInt(0, 3)));
        }

        if (RAND.nextFloat() < 0.02f) {
            this.spawnAtLocation(new ItemStack(DRItems.HARMONY_MUSIC_DISK.get()));
        }
        super.die(damageSource);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public static boolean canSpawn(EntityType<HelWalkerEntity> helWalkerEntityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        return checkMobSpawnRules(helWalkerEntityType, serverLevelAccessor, mobSpawnType, blockPos, randomSource) && serverLevelAccessor.getDifficulty() != Difficulty.PEACEFUL;
    }
}
