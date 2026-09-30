package com.deriys.divinerelics.entities.entity;

import com.deriys.divinerelics.capabilities.thor.FightsThorProvider;
import com.deriys.divinerelics.config.DivineRelicsCommonConfig;
import com.deriys.divinerelics.core.networking.DRMessages;
import com.deriys.divinerelics.core.networking.packets.ThorPlayMusicS2CPacket;
import com.deriys.divinerelics.core.networking.packets.ThorStopMusicS2CPacket;
import com.deriys.divinerelics.entities.ai.TimedAttack;
import com.deriys.divinerelics.entities.ai.thor.FireIgnoringPathNavigation;
import com.deriys.divinerelics.entities.ai.thor.ThorAttackGoal;
import com.deriys.divinerelics.entities.ai.thor.ThorAttackState;
import com.deriys.divinerelics.entities.ai.thor.ThorShieldKnockback;
import com.deriys.divinerelics.entities.client.SyncedAnimationController;
import com.deriys.divinerelics.init.DRItems;
import com.deriys.divinerelics.init.DRSounds;
import com.deriys.divinerelics.items.Motosignir;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.deriys.divinerelics.entities.entity.DraugrEntity.setChasing;
import static com.deriys.divinerelics.entities.entity.ThrownMjolnir.*;
import static com.deriys.divinerelics.items.DraupnirSpear.RAND;

public class ThorEntity extends Monster implements IAnimatable {
    public static final int INTRO_MUSIC_DURATION = 549;
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final AttributeModifier SLOW_SPEED_MODIFIER = new AttributeModifier("SlowSpeed", 0, AttributeModifier.Operation.MULTIPLY_BASE);
    private static final AttributeModifier FAST_SPEED_MODIFIER = new AttributeModifier("FastSpeed", DivineRelicsCommonConfig.THOR_SPEED_MODIFIER.get(), AttributeModifier.Operation.MULTIPLY_BASE);
    private static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(ThorEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Byte> ATTACK_STATE =
            SynchedEntityData.defineId(ThorEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> HAS_MJOLNIR_IN_HANDS =
            SynchedEntityData.defineId(ThorEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> WAITS_FOR_MJOLNIR =
            SynchedEntityData.defineId(ThorEntity.class, EntityDataSerializers.BOOLEAN);
    /** Game time at which the current attack started on the server: clients play its animation from there. */
    private static final EntityDataAccessor<Integer> ATTACK_START_TIME =
            SynchedEntityData.defineId(ThorEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SUMMONING_COMPLETE =
            SynchedEntityData.defineId(ThorEntity.class, EntityDataSerializers.BOOLEAN);

    private final ServerBossEvent bossEvent = new ServerBossEvent(Component.literal("Thor").withStyle(ChatFormatting.BOLD), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS);
    public static final int INVULNERABLE_TICKS = 549;
    public static final int[] LIGHTNING_TICKS = {
            149,
            223,
            323,
            394,
            462,
            481,
            516
    };
    public DamageSource thorDamageSource = DamageSource.mobAttack(this);
    public UUID thrownMjolnirUUID = null;
    /** Thor gives up on a thrown hammer that did not come back in time (removed, stuck in a non-ticking chunk...). */
    private static final int MJOLNIR_RETURN_TIMEOUT = 400;
    private int ticksWaitingForMjolnir = 0;

    private List<ServerPlayer> trackingPlayers = new ArrayList<>();
    public short ambientSoundCount = -1;

    public int ambientSoundInterval = this.getAmbientSoundInterval();

    public static final SoundEvent[] AMBIENT_SOUNDS = {
            DRSounds.THOR_AMBIENT_1.get(),
            DRSounds.THOR_AMBIENT_2.get(),
            DRSounds.THOR_AMBIENT_3.get(),
            DRSounds.THOR_AMBIENT_7.get(),
            DRSounds.THOR_AMBIENT_4.get(),
            DRSounds.THOR_AMBIENT_5.get(),
            DRSounds.THOR_AMBIENT_6.get(),
            DRSounds.THOR_AMBIENT_8.get(),
            DRSounds.THOR_AMBIENT_9.get(),
    };
    private List<UUID> needMusicReplay = new ArrayList<>();
    private List<UUID> initialPlayersNearby = new ArrayList<>();

    /** Server: the current attack's progress (see {@link #startAttack}). */
    private final TimedAttack attack = new TimedAttack();

    public ThorEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.setPersistenceRequired();
    }

    public void setAttacking(boolean attacking) {
//        Player nearestPlayer = this.level.getNearestPlayer(this, 30D);
//        if (nearestPlayer != null) {
//            nearestPlayer.sendSystemMessage(Component.literal("Attacking: " + attacking));
//        }
        this.entityData.set(ATTACKING, attacking);
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttackState(ThorAttackState state) {
//        Player nearestPlayer = this.level.getNearestPlayer(this, 30D);
//        if (nearestPlayer != null) {
//            nearestPlayer.sendSystemMessage(Component.literal("Changed attack state to: " + state));
//        }
        if (state != ThorAttackState.NONE) {
            setHasMjolnirInHands(state != ThorAttackState.CLAP_ATTACK && state != ThorAttackState.GROUND_ATTACK && state != ThorAttackState.LEG_ATTACK);
        }
        this.entityData.set(ATTACK_STATE, (byte) state.ordinal());
    }

    public ThorAttackState getAttackState() {
        return ThorAttackState.getState(this.entityData.get(ATTACK_STATE));
    }

    public void setHasMjolnirInHands(boolean hasMjolnir) {
        this.entityData.set(HAS_MJOLNIR_IN_HANDS, hasMjolnir);
    }

    public boolean hasMjolnirInHands() {
        return this.entityData.get(HAS_MJOLNIR_IN_HANDS);
    }

    public boolean waitsForMjolnir() {
        return this.entityData.get(WAITS_FOR_MJOLNIR);
    }

    public void setWaitsForMjolnir(boolean waitsForMjolnir) {
//        Player nearestPlayer = this.level.getNearestPlayer(this, 30D);
//        if (nearestPlayer != null) {
//            nearestPlayer.sendSystemMessage(Component.literal("WAITS FOR MJOLNIR: " + waitsForMjolnir));
//        }
        this.entityData.set(WAITS_FOR_MJOLNIR, waitsForMjolnir);
    }

    public int getAttackStartTime() {
        return this.entityData.get(ATTACK_START_TIME);
    }

    /**
     * Starts an attack. It plays out to the end on its own (see {@link #tickAttack}), whether or not Thor still has a
     * target by then: its animation and its impact always stay together.
     */
    public void startAttack(ThorAttackState state, @Nullable LivingEntity target) {
        this.setAttackState(state);
        this.setAttacking(true);
        this.entityData.set(ATTACK_START_TIME, (int) this.level.getGameTime());
        this.attack.start(target);
        this.playAttackSound(state);
    }

    /** Advances the current attack by a tick (server): it hits halfway through and ends with its animation. */
    private void tickAttack() {
        ThorAttackState state = this.getAttackState();
        if (this.attack.tick(this, state.getImpactTick(), state.getDurationTicks(), () -> this.performAttackImpact(state))) {
            this.setAttacking(false);
            if (state == ThorAttackState.MJOLNIR_THROW) {
                this.setAttackState(ThorAttackState.NONE);
            }
        }
    }

    private void performAttackImpact(ThorAttackState state) {
        LivingEntity target = this.attack.getTarget();
        switch (state) {
            case CLAP_ATTACK -> onHitLighnting(this.level, this, this.getOnPos(), this, this.thorDamageSource,
                    ThorAttackGoal.CLAP_ATTACK_DAMAGE, ThorAttackGoal.CLAP_ATTACK_FORCE, ThorAttackGoal.CLAP_ATTACK_RADUIS);
            case GROUND_ATTACK -> onHitLighnting(this.level, this, this.getOnPos(), this, this.thorDamageSource,
                    ThorAttackGoal.GROUND_ATTACK_DAMAGE, ThorAttackGoal.GROUND_ATTACK_FORCE, ThorAttackGoal.GROUND_ATTACK_RADUIS);
            case MELEE_ATTACK, LEG_ATTACK -> {
                this.swing(InteractionHand.MAIN_HAND);
                // The swing always happens; it only lands on a target that is still alive and within reach.
                if (target != null && this.attack.targetInReach(this, ThorAttackGoal.attackRangeSqr(this, target))) {
                    // The hammer swing has no knockback of its own, but a blocked one still throws the target back.
                    double force = state == ThorAttackState.LEG_ATTACK ? ThorAttackGoal.LEG_ATTACK_FORCE : 0.0D;
                    ThorShieldKnockback.knockBackAndHurt(target, this.position(), force,
                            strength -> Motosignir.applyKnockBack(target, this, strength), () -> this.doHurtTarget(target));
                }
            }
            case MJOLNIR_THROW -> {
                Vec3 aim = target != null && target.isAlive() ? target.position().add(0, target.getEyeHeight() / 1.5F, 0)
                        : this.attack.getLastTargetPos();
                if (aim != null && !this.waitsForMjolnir() && this.thrownMjolnirUUID == null) {
                    this.throwMjolnirAt(aim);
                }
            }
            default -> {
            }
        }
    }

    private void throwMjolnirAt(Vec3 aim) {
        this.setHasMjolnirInHands(false);
        this.setWaitsForMjolnir(true);
        ThrownMjolnir thrownMjolnir = new ThrownMjolnir(this.level, this, new ItemStack(DRItems.MJOLNIR.get()));
        thrownMjolnir.shootMjolnirAt(this, aim);
        thrownMjolnir.pickup = AbstractArrow.Pickup.DISALLOWED;
        thrownMjolnir.setNoGravity(true);
        this.level.addFreshEntity(thrownMjolnir);
        this.thrownMjolnirUUID = thrownMjolnir.getUUID();
        this.level.playSound(null, this.getOnPos(), DRSounds.MJOLNIR_THROWING.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
    }

    public void setSummoningComplete(boolean summoningComplete) {
        this.entityData.set(SUMMONING_COMPLETE, summoningComplete);
    }

    public boolean isSummoningComplete() {
        return this.entityData.get(SUMMONING_COMPLETE);
    }

    @Override
    protected void registerGoals () {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.addBehaviourGoals();
    }

    protected void addBehaviourGoals () {
        this.goalSelector.addGoal(1, new ThorAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Monster.class, true));
        this.goalSelector.addGoal(4, new FloatGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes () {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, DivineRelicsCommonConfig.THOR_HP.get())
                .add(Attributes.FOLLOW_RANGE, DivineRelicsCommonConfig.THOR_FOLLOW_RANGE.get())
                .add(Attributes.KNOCKBACK_RESISTANCE, DivineRelicsCommonConfig.THOR_KB_RESISTANCE.get())
                .add(Attributes.MOVEMENT_SPEED, 0.23F)
                .add(Attributes.ATTACK_DAMAGE, DivineRelicsCommonConfig.THOR_DAMAGE.get())
                .add(Attributes.ARMOR, DivineRelicsCommonConfig.THOR_ARMOR.get())
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING, false);
        this.entityData.define(ATTACK_STATE, (byte) 0);
        this.entityData.define(HAS_MJOLNIR_IN_HANDS, true);
        this.entityData.define(WAITS_FOR_MJOLNIR, false);
        this.entityData.define(ATTACK_START_TIME, 0);
        this.entityData.define(SUMMONING_COMPLETE, false);
    }

    /**
     * This method is responsible for the periodic updates of the ThorEntity. It performs various checks and updates based
     * on the current state of the entity.
     *
     * It updates the boss bar's progress according to the health state of the entity.
     *
     * If the summoning is complete and the level is not client-side, it handles player tracking and ambient sound logic for
     * aggressive behavior.
     *
     * The method also manages the introduction and looping of fight music for players in the vicinity.
     *
     * In case the entity is waiting for its thrown Mjölnir to return, the method updates its state based on Mjölnir's
     * position and status.
     *
     * During the summoning phase, it spawns lightning strikes at specified ticks and sets the entity to a non-invulnerable
     * state once summoning is complete.
     *
     * It also sets the entity to be in a chasing state if there is a target.
     *
     * The method reduces the attacking ticks if there are any left and calls the superclass tick method at the end.
     */
    @Override
    public void tick() {
        boolean summoningComplete = this.isSummoningComplete();
        if (!this.level.isClientSide) {
            // While Thor is being summoned, his bar fills up as he arrives; afterwards it shows his health.
            float bossBarProgress = summoningComplete ? this.getHealth() / this.getMaxHealth()
                    : Mth.clamp((float) this.tickCount / INVULNERABLE_TICKS, 0.0F, 1.0F);
            if (this.bossEvent.getProgress() != bossBarProgress) {
                this.bossEvent.setProgress(bossBarProgress);
            }
            // Players who can see Thor get his bar once they are within 50 blocks.
            this.trackingPlayers.removeIf(trackingPlayer -> {
                if (trackingPlayer.position().distanceToSqr(this.position()) < 2500) {
                    this.bossEvent.addPlayer(trackingPlayer);
                    return true;
                }
                return false;
            });
        }

        if (!this.level.isClientSide && summoningComplete) {

            LivingEntity target = this.getTarget();
            if (this.isAggressive() && !(target instanceof Mob)) {
                if (this.isAlive() && this.random.nextInt(1000) < this.ambientSoundInterval++) {
                    this.resetAmbientInterval();
                    // floorMod on the array length: the persisted short used to overflow into a negative index.
                    this.ambientSoundCount = (short) Math.floorMod(this.ambientSoundCount + 1, AMBIENT_SOUNDS.length);
                    this.playSound(AMBIENT_SOUNDS[this.ambientSoundCount], this.getSoundVolume(), this.getVoicePitch());
                }
            }

            if (this.tickCount >= INTRO_MUSIC_DURATION && !this.needMusicReplay.isEmpty()) {
                ServerLevel serverLevel = (ServerLevel) level;
                List<UUID> copyList = List.copyOf(this.needMusicReplay);
                for (UUID playerUUID: copyList) {
                    if (serverLevel.getEntity(playerUUID) instanceof ServerPlayer player) {
                        stopSoundForPlayer(player, DRSounds.THOR_FIGHT_MUSIC.get(), SoundSource.RECORDS);
                        stopSoundForPlayer(player, DRSounds.THOR_FIGHT_MUSIC_LOOP.get(), SoundSource.RECORDS);
                        playSoundForPlayer(player, DRSounds.THOR_FIGHT_MUSIC_LOOP.get(), this.getOnPos());
                        if (!initialPlayersNearby.contains(playerUUID)) {
                            this.addInitialPlayersNearby(player);
                        }
                        this.needMusicReplay.remove(playerUUID);
                    }
                }
            }
        }

        if (!this.level.isClientSide && this.waitsForMjolnir()) {
            ServerLevel serverLevel = (ServerLevel) level;
            Entity thrownEntity = this.thrownMjolnirUUID == null ? null : serverLevel.getEntity(this.thrownMjolnirUUID);
            if (thrownEntity instanceof ThrownMjolnir thrownMjolnir) {
                if (thrownMjolnir.isOnGround() || thrownMjolnir.hasHit()) {
                    thrownMjolnir.setReturning(true);
                }
                if (thrownMjolnir.isReturning() && this.position().distanceToSqr(thrownMjolnir.position()) < 15) {
                    this.catchMjolnir(thrownMjolnir);
                }
            }
            // Without a timeout a lost hammer left Thor waiting forever: he never threw again (boss soft-lock).
            if (this.waitsForMjolnir() && ++this.ticksWaitingForMjolnir > MJOLNIR_RETURN_TIMEOUT) {
                this.catchMjolnir(thrownEntity);
            }
        } else {
            this.ticksWaitingForMjolnir = 0;
        }

        BlockPos onPos = this.getOnPos();

        if (!summoningComplete && !this.level.isClientSide) {
            if (this.tickCount < INVULNERABLE_TICKS) {
                if (contains(LIGHTNING_TICKS, this.tickCount)) {
                    spawnLightning(this.level, getRandBlockPos(onPos.offset(0, -10, 0), 100, 100, 50), this);
                }
            } else if (this.tickCount == INVULNERABLE_TICKS) {
                this.teleportTo(this.getX(), this.getY() - 10d, this.getZ());
                spawnLightning(this.level, onPos, this);
                onHitLighnting(this.level, this, onPos, this, this.thorDamageSource, 10f, 2f, 12);
                this.setSummoningComplete(true);
                this.setNoAi(false);
            }
        }
        LivingEntity target = this.getTarget();
        setChasing(this, FAST_SPEED_MODIFIER, SLOW_SPEED_MODIFIER, target != null && target.isAlive());

        super.tick();

        // After the goals ran, so an attack started this tick counts this tick as its first (tick 0).
        if (!this.level.isClientSide && this.isAlive()) {
            this.tickAttack();
        }
    }

    public boolean canSeeTarget(LivingEntity target) {
        Vec3 startPos = this.position().add(0, this.getEyeHeight(), 0);
        Vec3 endPos = target.position().add(0, target.getEyeHeight(), 0);
        Level level = this.level;

        ClipContext context = new ClipContext(startPos, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this);
        HitResult result = level.clip(context);

        return result.getType() == HitResult.Type.MISS;
    }

    private void catchMjolnir(@Nullable Entity thrownMjolnir) {
        ThorAttackState attackState = this.getAttackState();
        if (attackState != ThorAttackState.CLAP_ATTACK && attackState != ThorAttackState.GROUND_ATTACK) {
            this.setHasMjolnirInHands(true);
        }
        if (thrownMjolnir != null) {
            thrownMjolnir.discard();
        }
        this.setWaitsForMjolnir(false);
        this.thrownMjolnirUUID = null;
        this.ticksWaitingForMjolnir = 0;
    }

    public static boolean contains(int[] arr, int value) {
        // Plain loop: called every tick during the summoning, a stream + lambda allocated each time.
        for (int element : arr) {
            if (element == value) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.tickCount = compoundTag.getInt("TickCount");
        this.ambientSoundCount = compoundTag.getShort("AmbientSoundCount");
        this.setSummoningComplete(compoundTag.getBoolean("SummoningComplete"));
        this.setAttacking(compoundTag.getBoolean("Attacking"));
        this.setAttackState(ThorAttackState.getState(compoundTag.getByte("AttackState")));
        this.setHasMjolnirInHands(compoundTag.getBoolean("HasMjolnirInHands"));
        // Resumes an attack that was under way when Thor was saved, at the same point of it.
        if (this.isAttacking() && compoundTag.contains("AttackTick") && compoundTag.getInt("AttackTick") >= 0) {
            this.attack.resume(compoundTag.getInt("AttackTick"));
            this.entityData.set(ATTACK_START_TIME, (int) this.level.getGameTime() - this.attack.getTick());
        } else {
            this.setAttacking(false);
        }
        if (compoundTag.hasUUID("ThrownMjolnirUUID")) {
            this.thrownMjolnirUUID = compoundTag.getUUID("ThrownMjolnirUUID");
        }
        this.setWaitsForMjolnir(compoundTag.getBoolean("WaitsForMjolnir"));

        ListTag needMusicReplayList = compoundTag.getList("NeedMusicReplay", 8);
        this.needMusicReplay = new ArrayList<>();
        for (int i = 0; i < needMusicReplayList.size(); i++) {
            this.needMusicReplay.add(UUID.fromString(needMusicReplayList.getString(i)));
        }

        ListTag initialPlayerNearbyList = compoundTag.getList("initialPlayersNearby", 8);
        this.initialPlayersNearby = new ArrayList<>();
        for (int i = 0; i < initialPlayerNearbyList.size(); i++) {
            this.initialPlayersNearby.add(UUID.fromString(initialPlayerNearbyList.getString(i)));
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("SummoningComplete", this.isSummoningComplete());
        compoundTag.putBoolean("Attacking", this.isAttacking());
        compoundTag.putBoolean("HasMjolnirInHands", this.entityData.get(HAS_MJOLNIR_IN_HANDS));
        compoundTag.putBoolean("WaitsForMjolnir", this.waitsForMjolnir());
        compoundTag.putInt("AttackTick", this.attack.getTick());
        compoundTag.putInt("TickCount", this.tickCount);
        compoundTag.putShort("AmbientSoundCount", this.ambientSoundCount);
        compoundTag.putByte("AttackState", this.entityData.get(ATTACK_STATE));
        if (this.thrownMjolnirUUID != null) {
            compoundTag.putUUID("ThrownMjolnirUUID", this.thrownMjolnirUUID);
        }
        ListTag needMusicReplayList = new ListTag();
        for (UUID uuid : this.needMusicReplay) {
            needMusicReplayList.add(StringTag.valueOf(uuid.toString()));
        }
        compoundTag.put("NeedMusicReplay", needMusicReplayList);
        ListTag initialPlayersNearbyList = new ListTag();
        for (UUID uuid : this.initialPlayersNearby) {
            initialPlayersNearbyList.add(StringTag.valueOf(uuid.toString()));
        }
        compoundTag.put("initialPlayersNearby", initialPlayersNearbyList);
    }

    @Override
    public void checkDespawn() { }

    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer serverPlayer) {
        super.startSeenByPlayer(serverPlayer);
        if (!this.isSummoningComplete()) {
            this.trackingPlayers.add(serverPlayer); // gets the (filling) bar once within 50 blocks, see tick()
        } else {
            this.bossEvent.addPlayer(serverPlayer);
        }

        UUID playerUUID = serverPlayer.getUUID();
        boolean containsInInitialList = this.initialPlayersNearby.contains(playerUUID);
        if (!containsInInitialList && !this.needMusicReplay.contains(playerUUID)) { // if the player sees thor for the first time (should be after the summoning is complete)
            this.needMusicReplay.add(playerUUID);
        } else if (containsInInitialList && !this.needMusicReplay.contains(playerUUID)) { // if the player saw thor, died and spawned far away, logged out and now sees him again
            serverPlayer.getCapability(FightsThorProvider.FIGHTS_THOR).ifPresent(fightsThor -> {
                List<String> thorList = fightsThor.getThorList();
                if (!thorList.isEmpty()) {
                    for (String thorStringUUID: thorList) {
                        if (UUID.fromString(thorStringUUID).equals(this.getUUID()) && fightsThor.hasLoggedOut()) {
                            this.needMusicReplay.add(playerUUID);
                            fightsThor.setHasLoggedOut(false);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer serverPlayer) {
        super.stopSeenByPlayer(serverPlayer);
        this.bossEvent.removePlayer(serverPlayer);
        // Otherwise a player who left during the summoning stays referenced and is added to the boss bar afterwards.
        this.trackingPlayers.remove(serverPlayer);
    }

    public void startPlayingBossMusic() {
        List<Player> playersNearby = Motosignir.getEntitiesInAreaOfClass(Player.class, this.level, this, 50);
        if (!playersNearby.isEmpty()) {
            for (Player player: playersNearby) {
                this.addInitialPlayersNearby(player);
            }
        }
        ItemStack musicDisc = new ItemStack(DRItems.THOR_FIGHT_MUSIC_DISC.get());
        BlockPos onPos = this.getOnPos();
        level.levelEvent(null, 1010, onPos, Item.getId(musicDisc.getItem()));
    }

    private void addInitialPlayersNearby(Player player) {
        player.getCapability(FightsThorProvider.FIGHTS_THOR).ifPresent(fightsThor -> {
            fightsThor.addThor(this.getStringUUID());
        });
        this.initialPlayersNearby.add(player.getUUID());
    }

    public void stopPlayingBossMusic() {
        if (!this.initialPlayersNearby.isEmpty()) {
            ServerLevel serverLevel = (ServerLevel) this.level;
            for (UUID playerUUID: initialPlayersNearby) {
                if (serverLevel.getEntity(playerUUID) instanceof ServerPlayer serverPlayer) {
                    stopSoundForPlayer(serverPlayer, DRSounds.THOR_FIGHT_MUSIC.get(), SoundSource.RECORDS);
                    stopSoundForPlayer(serverPlayer, DRSounds.THOR_FIGHT_MUSIC_LOOP.get(), SoundSource.RECORDS);
                }
            }
        }
    }

    public static void playSoundForPlayer(ServerPlayer player, SoundEvent soundEvent, BlockPos blockPos) {
        DRMessages.sendToPlayer(new ThorPlayMusicS2CPacket(soundEvent.getLocation(), blockPos.getX(), blockPos.getY(), blockPos.getZ()), player);
    }

    public static void stopSoundForPlayer(ServerPlayer player, SoundEvent soundEvent, SoundSource soundSource) {
        DRMessages.sendToPlayer(new ThorStopMusicS2CPacket(soundEvent.getLocation(), soundSource), player);
    }

    @Override
    public void die(@NotNull DamageSource damageSource) {
        if (damageSource.getEntity() instanceof Player) {
            this.spawnAtLocation(new ItemStack(DRItems.PERFECT_ASGARDIAN_STEEL_INGOT.get(), 4));

            if (RAND.nextFloat() < 0.1) {
                this.spawnAtLocation(new ItemStack(DRItems.ASGARDIAN_STEEL_NUGGET.get(), RAND.nextInt(3, 7)));
            }

            if (RAND.nextFloat() < 0.02) {
                this.spawnAtLocation(new ItemStack(DRItems.THOR_FIGHT_MUSIC_DISC.get()));
            }
        }

        super.die(damageSource);
        this.bossEvent.setVisible(false);

        if (this.level instanceof ServerLevel serverLevel) {
            removeThorFromPlayerCapability(serverLevel);
            serverLevel.setWeatherParameters(0, 0, false, false);
            stopPlayingBossMusic();
            this.level.playSound(null, this.getOnPos(), DRSounds.THOR_DEATH_SOUND.get(), SoundSource.HOSTILE, 2.0f, 1.0f);
        }
    }

    private void removeThorFromPlayerCapability(ServerLevel serverLevel) {
        if (!this.initialPlayersNearby.isEmpty()) {
            for (UUID playerUUID: this.initialPlayersNearby) {
                if (serverLevel.getEntity(playerUUID) instanceof Player player) {
                    player.getCapability(FightsThorProvider.FIGHTS_THOR).ifPresent(fightsThor -> {
                        fightsThor.removeThor(this.getStringUUID());
                    });
                }
            }
        }
    }

    @Override
    public int getExperienceReward() {
        return DivineRelicsCommonConfig.THOR_XP_AMOUNT.get();
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        ThorAttackState state = this.getAttackState();
        if (this.isAttacking() && state.getAnimationName() != null) {
            // Where the server is in this attack. GeckoLib only runs this while Thor is drawn, so an attack that
            // started while nobody looked at him is picked up part-way, at the same point as its server-side hit.
            int startTime = this.getAttackStartTime();
            double elapsed = SyncedAnimationController.ticksSince(this.level, startTime, event.getPartialTick());
            if (elapsed < state.getDurationTicks()) {
                if (event.getController() instanceof SyncedAnimationController<?> controller) {
                    controller.playSynced(state.getAnimationName(), startTime, elapsed);
                }
                return PlayState.CONTINUE;
            }
        }

        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.thor.walk", ILoopType.EDefaultLoopTypes.LOOP));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.thor.idle", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }

    public void playAttackSound(ThorAttackState attackState) {
        SoundEvent soundEvent = null;
        switch (attackState) {
            case MELEE_ATTACK -> soundEvent = DRSounds.THOR_MELEE_ATTACK.get();
            case LEG_ATTACK -> soundEvent = DRSounds.THOR_LEG_ATTACK.get();
            case CLAP_ATTACK -> soundEvent = DRSounds.THOR_CLAP_ATTACK.get();
            case GROUND_ATTACK -> soundEvent = DRSounds.THOR_GROUND_ATTACK.get();
            case MJOLNIR_THROW -> soundEvent = DRSounds.THOR_MJOLNIR_THROW_ATTACK.get();
        }
        if (soundEvent != null) {
            this.level.playSound(null, this.getOnPos(), soundEvent, SoundSource.HOSTILE, 1.0f, RAND.nextFloat() * 0.1F + 0.95F);
        }
    }

    @Override
    public void registerControllers (AnimationData animationData){
        animationData.addAnimationController(new SyncedAnimationController<>(this, "controller",
                0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory () {
        return this.factory;
    }

    @Override
    protected PathNavigation createNavigation(Level p_21480_) {
        return new FireIgnoringPathNavigation(this, level);
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean isInvisible() {
        if (!this.isSummoningComplete()) {
            return true;
        }
        return super.isInvisible();
    }

    @Override
    public boolean isInvulnerable() {
        if (!this.isSummoningComplete()) {
            return true;
        }
        return super.isInvulnerable();
    }

    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        if (!this.isSummoningComplete()) {
            return false;
        }
        return super.hurt(p_21016_, p_21017_);
    }

//    @Override
//    public boolean isNoAi() {
//        return !this.isSummoningComplete();
//    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    private void resetAmbientInterval() {
        this.ambientSoundInterval = -this.getAmbientSoundInterval();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 200;
    }

    @Override
    public void setTarget(@Nullable LivingEntity newTarget) {
        if (!(this.getTarget() instanceof Player player) || newTarget != null || !player.isAlive() || player.getAbilities().instabuild) {
            super.setTarget(newTarget);
        }
    }

    @Override
    public float getVoicePitch() {
        return 1f;
    }
}