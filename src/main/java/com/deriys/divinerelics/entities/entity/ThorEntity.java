package com.deriys.divinerelics.entities.entity;

import com.deriys.divinerelics.capabilities.thor.FightsThorProvider;
import com.deriys.divinerelics.config.DivineRelicsCommonConfig;
import com.deriys.divinerelics.core.networking.DRMessages;
import com.deriys.divinerelics.core.networking.packets.ThorPlayMusicS2CPacket;
import com.deriys.divinerelics.core.networking.packets.ThorStopMusicS2CPacket;
import com.deriys.divinerelics.entities.ai.thor.FireIgnoringPathNavigation;
import com.deriys.divinerelics.entities.ai.thor.ThorAttackGoal;
import com.deriys.divinerelics.entities.ai.thor.ThorAttackState;
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
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
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
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.Arrays;
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
    private static final EntityDataAccessor<Integer> ATTACKING_TICKS =
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

    public void setAttackingTicks(int ticks) {
        this.entityData.set(ATTACKING_TICKS, ticks);
    }

    public int getAttackingTicks() {
        return this.entityData.get(ATTACKING_TICKS);
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
        this.entityData.define(ATTACKING_TICKS, 0);
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

        float bossBarProgress = this.getHealth() / this.getMaxHealth();
        if (this.bossEvent.getProgress() != bossBarProgress){
            this.bossEvent.setProgress(bossBarProgress);
        }

        boolean summoningComplete = this.isSummoningComplete();
        if (!this.level.isClientSide && summoningComplete) {

            if (!this.trackingPlayers.isEmpty()) {
                for (ServerPlayer trackingPlayer: this.trackingPlayers) {
                    if (trackingPlayer.position().distanceToSqr(this.position()) < 2500) {
                        this.bossEvent.addPlayer(trackingPlayer);
                    }
                }
                this.trackingPlayers = new ArrayList<>();
            }

            LivingEntity target = this.getTarget();
            if (this.isAggressive() && !(target instanceof Mob)) {
                if (this.isAlive() && this.random.nextInt(1000) < this.ambientSoundInterval++) {
                    this.resetAmbientInterval();
                    this.playSound(AMBIENT_SOUNDS[++this.ambientSoundCount % 9], this.getSoundVolume(), this.getVoicePitch());
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
            if (serverLevel.getEntity(this.thrownMjolnirUUID) instanceof ThrownMjolnir thrownMjolnir) {
                if (thrownMjolnir.isOnGround() || thrownMjolnir.hasHit()) {
                    thrownMjolnir.setReturning(true);
                }
                if (thrownMjolnir.isReturning() && this.position().distanceToSqr(thrownMjolnir.position()) < 15) {
                    ThorAttackState attackState = this.getAttackState();
                    if (attackState != ThorAttackState.CLAP_ATTACK && attackState != ThorAttackState.GROUND_ATTACK) {
                        this.setHasMjolnirInHands(true);
                    }
                    thrownMjolnir.discard();
                    this.setWaitsForMjolnir(false);
                    this.thrownMjolnirUUID = null;
                }

            }
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

        int attackingTicks = this.getAttackingTicks();

        if (attackingTicks > 0) {
            this.setAttackingTicks(attackingTicks - 1);
        }
        super.tick();
    }

    public boolean canSeeTarget(LivingEntity target) {
        Vec3 startPos = this.position().add(0, this.getEyeHeight(), 0);
        Vec3 endPos = target.position().add(0, target.getEyeHeight(), 0);
        Level level = this.level;

        ClipContext context = new ClipContext(startPos, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this);
        HitResult result = level.clip(context);

        return result.getType() == HitResult.Type.MISS;
    }

    public static boolean contains(int[] arr, int value) {
        return Arrays.stream(arr).anyMatch(x -> x == value);
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
        this.setAttackingTicks(compoundTag.getInt("AttackingTicks"));
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
        compoundTag.putInt("AttackingTicks", this.getAttackingTicks());
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
            this.trackingPlayers.add(serverPlayer);
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
        if(this.isAttacking() && this.getAttackingTicks() == 0) {
            event.getController().markNeedsReload();
            switch (this.getAttackState()){
                case MELEE_ATTACK -> setAttackAnimation(event, "animation.thor.melee_attack", 24);
                case LEG_ATTACK -> setAttackAnimation(event, "animation.thor.leg_attack", 30);
                case CLAP_ATTACK -> setAttackAnimation(event, "animation.thor.clap_attack", 48);
                case GROUND_ATTACK -> setAttackAnimation(event, "animation.thor.ground_attack", 50);
                case MJOLNIR_THROW -> setAttackAnimation(event, "animation.thor.mjolnir_throw_attack", 30);
            }
            return PlayState.CONTINUE;
        }

        if (this.getAttackingTicks() == 0) {
            if (event.isMoving()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.thor.walk", ILoopType.EDefaultLoopTypes.LOOP));
                return PlayState.CONTINUE;
            }
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.thor.idle", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> void setAttackAnimation(AnimationEvent<E> event, String animationName, int ticks) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(animationName, ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        this.setAttackingTicks(ticks);
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
        animationData.addAnimationController(new AnimationController(this, "controller",
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