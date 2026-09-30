package com.deriys.divinerelics.entities.entity;

import com.deriys.divinerelics.capabilities.mjolnir.MjolnirBindingProvider;
import com.deriys.divinerelics.config.DivineRelicsCommonConfig;
import com.deriys.divinerelics.entities.ai.thor.ThorShieldKnockback;
import com.deriys.divinerelics.init.DREntitiyTypes;
import com.deriys.divinerelics.init.DRItems;
import com.deriys.divinerelics.items.Motosignir;
import com.deriys.divinerelics.init.DRSounds;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static com.deriys.divinerelics.capabilities.teammates.TeammatesProvider.hasTeammate;
import static com.deriys.divinerelics.items.HeimdallGauntlet.RAND;

public class ThrownMjolnir extends AbstractArrow {
    private static final EntityDataAccessor<Boolean> ID_FOIL;
    private static final EntityDataAccessor<Boolean> SHOULD_RETURN;
    private ItemStack mjolnirItem;
    private boolean dealtDamage;
    private boolean hit;
    public static final double STRIKE_DAMAGE = DivineRelicsCommonConfig.THROWN_MJOLNIR_STRIKE_DAMAGE.get();
    private static final int COOLDOWN = DivineRelicsCommonConfig.MJOLNIR_THROW_COOLDOWN.get();
    public static double STRIKE_RADIUS = DivineRelicsCommonConfig.THROWN_MJOLNIR_STRIKE_RADIUS.get();
    public static double STRIKE_FORCE = DivineRelicsCommonConfig.THROWN_MJOLNIR_STRIKE_FORCE.get();
    public static double RETURNING_SPEED = DivineRelicsCommonConfig.THROWN_MJOLNIR_RETURNING_SPEED.get();
    public int clientSideReturnTridentTickCount;
    public boolean shouldReturn = false;
    public boolean relaxed = false;
    /** Set for hammers thrown by Thor, so orphans (Thor unloaded or gone) can be cleaned up instead of lingering. */
    private boolean thrownByThor = false;
    /** Grace period before a Thor hammer without a resolvable owner is removed (the owner may load a bit later). */
    private static final int ORPHAN_GRACE_TICKS = 100;
    /** Entities already hit directly during the current return flight, so each one is hit at most once per return. */
    private final IntOpenHashSet returnHitEntityIds = new IntOpenHashSet();
    /**
     * Entities that already took the area strike (lightning, area damage, knockback) in the current flight direction;
     * each one takes it at most once per direction. Before, every hit struck all mobs in range again, so flying through
     * a crowd spawned thousands of lightning bolts. Each bolt checks every entity around it, other bolts included, for
     * vanilla advancements, which froze the server until it ran out of memory.
     */
    private final IntOpenHashSet areaStruckEntityIds = new IntOpenHashSet();

    public ThrownMjolnir(EntityType<? extends ThrownMjolnir> entityType, Level level) {
        super(entityType, level);
        this.mjolnirItem = new ItemStack(DRItems.MJOLNIR.get());
    }

    public ThrownMjolnir(Level p_37569_, LivingEntity p_37570_, ItemStack p_37571_) {
        super(DREntitiyTypes.THROWN_MJOLNIR.get(), p_37570_, p_37569_);
        this.mjolnirItem = new ItemStack(DRItems.MJOLNIR.get());
        this.mjolnirItem = p_37571_.copy();
        this.entityData.set(ID_FOIL, p_37571_.hasFoil());
        this.entityData.set(SHOULD_RETURN, this.shouldReturn);
        this.thrownByThor = p_37570_ instanceof ThorEntity;
    }

    /**
     * Built on demand instead of once in a field initializer: entities loaded from disk (chunk reload, restart) have no
     * owner yet while their fields are initialised, so the cached source attributed all later damage to the hammer
     * itself (no kill credit/XP, wrong death messages, teammate protection bypassed).
     */
    public DamageSource getDamageSource() {
        Entity owner = this.getOwner();
        return DamageSource.trident(this, owner == null ? this : owner);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_FOIL, false);
        this.entityData.define(SHOULD_RETURN, false);
    }

    public void tick() {
        if (!this.level.isClientSide && this.isAbandonedThorHammer()) {
            this.discard();
            return;
        }

        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        if (!this.level.isClientSide) {
            if (this.isAcceptibleReturnOwner() && !this.isReturning()) {
                if (this.position().distanceToSqr(this.getOwner().position()) > 10000) {
                    this.setReturning(true);
                    this.relaxed = false;
                }
            }
        }

        if (this.getOwner() instanceof LivingEntity owner) {
            if (!this.isAcceptibleReturnOwner() && !this.level.isClientSide && !this.relaxed && this.pickup == Pickup.ALLOWED) {
                this.pickup = Pickup.DISALLOWED;
                this.relax();
            } else if (!this.level.isClientSide && owner.isAlive() && this.pickup == Pickup.DISALLOWED) {
                this.pickup = Pickup.ALLOWED;
            } else if (this.isReturning()) {
                this.relaxed = false;
                this.setNoPhysics(true);
                Vec3 pathStart = this.position();
                Vec3 vectorPM = owner.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vectorPM.y * 0.015 * 4, this.getZ());
                if (this.level.isClientSide) {
                    this.yOld = this.getY();
                }

                double fact = 0.05 * RETURNING_SPEED;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add(vectorPM.normalize().scale(fact)));
                if (!this.level.isClientSide && owner instanceof Player player) {
                    // super.tick() moves the hammer by exactly this delta, so this is the path it covers this tick.
                    Vec3 pathEnd = this.position().add(this.getDeltaMovement());
                    ReturnFlight.smashBlocks(this, player, pathStart, pathEnd);
                    ReturnFlight.hitEntities(this, player, pathStart, pathEnd, this.returnHitEntityIds, this::canHitEntity, this::onHitEntity);
                }
                if (this.clientSideReturnTridentTickCount == 0) {
                    this.playSound(DRSounds.MJOLNIR_RETURN.get(), 15.0F, 1.0F);
                }

                ++this.clientSideReturnTridentTickCount;
            }
        }
        super.tick();

        if (this.inGround && !this.isNoPhysics()) {
            // It has come to rest in a block. AbstractArrow keeps the last flight movement around, and a recall would
            // otherwise carry that on in the throw direction (through the terrain) before turning back to the owner.
            this.setDeltaMovement(Vec3.ZERO);
        }
    }

    /**
     * A hammer thrown by Thor is only valid while that Thor is alive and still waiting for exactly this hammer. Before,
     * only a loaded, dead Thor released his hammer; one whose Thor was unloaded (or no longer waiting for it) could
     * linger in the world indefinitely.
     */
    private boolean isAbandonedThorHammer() {
        Entity owner = this.getOwner();
        if (owner instanceof ThorEntity thor) {
            return !thor.isAlive() || !this.getUUID().equals(thor.thrownMjolnirUUID);
        }
        return this.thrownByThor && this.tickCount > ORPHAN_GRACE_TICKS;
    }

    public boolean isReturning() {
        return this.entityData.get(SHOULD_RETURN);
    }

    public void setReturning(boolean returning) {
        if (returning && !this.isReturning()) {
            // The return is a new flight direction: everything may be hit and struck once more.
            this.returnHitEntityIds.clear();
            this.areaStruckEntityIds.clear();
        }
        this.entityData.set(SHOULD_RETURN, returning);
    }

    private boolean isAcceptibleReturnOwner() {
        Entity $$0 = this.getOwner();
        if ($$0 != null && $$0.isAlive()) {
            return !($$0 instanceof ServerPlayer) || !$$0.isSpectator();
        } else {
            return false;
        }
    }

    private void relax() {
        this.setNoGravity(false);
        this.setNoPhysics(false);
        this.setDeltaMovement(this.getDeltaMovement().multiply(0.001, 0.01, 0.001));
        this.setReturning(false);
        this.hit = true; // so that it won't spawn lightnings
        this.relaxed = true;
        this.clientSideReturnTridentTickCount = 0;
        this.pickup = Pickup.DISALLOWED;
    }

    protected ItemStack getPickupItem() {
        return this.mjolnirItem.copy();
    }

    public boolean isFoil() {
        return false;
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 p_37575_, Vec3 p_37576_) {
        return super.findHitEntity(p_37575_, p_37576_);
    }

    protected void onHitEntity(EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();
        double damage = DivineRelicsCommonConfig.THROWN_MJOLNIR_DAMAGE.get();
        if (entity instanceof LivingEntity hitEntity) {
            damage += EnchantmentHelper.getDamageBonus(this.mjolnirItem, hitEntity.getMobType());
        }

        Entity owner = this.getOwner();
        float volume = 1.0F;
        this.dealtDamage = true;
        SoundEvent soundEvent = DRSounds.MJOLNIR_IMPACT.get();
        float amount = (float) damage;
        // Whoever blocks the flying hammer with a shield is still thrown back by it, whoever threw it.
        boolean hurt = entity instanceof LivingEntity livingTarget && !this.level.isClientSide
                ? ThorShieldKnockback.knockBackAndHurt(livingTarget, this.position(), 0.0D, strength -> {
                }, () -> livingTarget.hurt(this.getDamageSource(), amount))
                : entity.hurt(this.getDamageSource(), amount);
        if (hurt) {
            this.hit = true;
            Level level = this.level;
            BlockPos blockPos = entity.blockPosition();
            if (!level.isClientSide && owner instanceof LivingEntity livingEntity) {
                mjolnirHit(livingEntity, level, blockPos, entity);
            }
            soundEvent = DRSounds.MJOLNIR_THUNDER.get();
            volume = 5.0F;
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }
            if (entity instanceof LivingEntity livingEntityHit) {
                if (owner instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingEntityHit, owner);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)owner, livingEntityHit);
                }

                this.doPostHurtEffects(livingEntityHit);
            }
        }

        this.playSound(soundEvent, volume, 1.0F);
    }

    @Override
    public int getKnockback() {
        return 3;
    }

    public static BlockPos getRandBlockPos(BlockPos blockPos) {
        return new BlockPos(blockPos.getX() + RAND.nextFloat() * 10 - 5, blockPos.getY(), blockPos.getZ() + RAND.nextFloat() * 10 - 5);
    }

    public static BlockPos getRandBlockPos(BlockPos blockPos, int x, int z, int origin) {
        return new BlockPos(blockPos.getX() + genRandSign(origin, x), blockPos.getY(), blockPos.getZ() + genRandSign(origin, z));
    }

    public static int genRandSign(int origin, int bound) {
        if (RAND.nextBoolean()) {
            return RAND.nextInt(origin, bound);
        } else {
            return -RAND.nextInt(origin, bound);
        }
    }

    public static void spawnLightning(Level level, BlockPos blockPos, Entity owner) {
        LightningBolt lightningBolt = (LightningBolt)EntityType.LIGHTNING_BOLT.create(level);
        lightningBolt.moveTo(Vec3.atBottomCenterOf(blockPos));
        lightningBolt.setCause(owner instanceof ServerPlayer ? (ServerPlayer)owner : null);
        level.addFreshEntity(lightningBolt);
    }

    public static void onHitLighnting(Level level, Entity hammer, BlockPos blockPos, LivingEntity owner, DamageSource damageSource, double damage, double force, double strikeRadius) {
        onHitLighnting(level, hammer, blockPos, owner, damageSource, damage, force, strikeRadius, null);
    }

    /**
     * Damages, knocks back and strikes with lightning every target around the hammer.
     *
     * @param alreadyStruck ids of entities that must not be struck again; the ones struck now are added to it.
     *                      {@code null} strikes every target in range.
     */
    public static void onHitLighnting(Level level, Entity hammer, BlockPos blockPos, LivingEntity owner, DamageSource damageSource,
                                      double damage, double force, double strikeRadius, @Nullable IntSet alreadyStruck) {
        List<LivingEntity> entitiesInArea =
                Motosignir.getEntitiesInArea(level, hammer.getX(), hammer.getY(), hammer.getZ(), strikeRadius);

        int targetsInArea = 0;
        List<LivingEntity> struckNow = new ArrayList<>();
        for (LivingEntity entity : entitiesInArea) {
            if (entity != owner && !hasTeammate(owner, entity)) {
                targetsInArea++;
                if (alreadyStruck == null || alreadyStruck.add(entity.getId())) {
                    struckNow.add(entity);
                }
            }
        }

        Motosignir.hurtAndKnockbackEntites(struckNow, owner, hammer, damageSource, damage, force);
        for (LivingEntity entity : struckNow) {
            spawnLightning(level, entity.getOnPos(), owner);
        }

        // Tops up sparse strikes (e.g. into the ground) with random bolts within 5 blocks. Targets that were struck
        // earlier count as well, so a crowd that already got its lightning gets no random bolts instead.
        int maxLightnings = DivineRelicsCommonConfig.THROWN_MJOLNIR_LIGHNTING_COUNT.get();
        for (int i = targetsInArea; i < maxLightnings; i++) {
            spawnLightning(level, getRandBlockPos(blockPos), owner);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        BlockPos blockPos = blockHitResult.getBlockPos();
        Vec3 prevMovement = this.getDeltaMovement();
        Vec3 position = new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        Level level = this.level;

        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), DRSounds.MJOLNIR_IMPACT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

        if (!level.isClientSide) {
            boolean isBedrock = level.getBlockState(blockPos).getBlock() == Blocks.BEDROCK;
            boolean isObsidian = level.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN;
            Entity owner = this.getOwner();

            if (owner instanceof LivingEntity livingEntity) {
                if (!this.hit && !isBedrock) {
                    // Players always break blocks; mobs (Thor) follow the mobGriefing game rule like vanilla mobs do.
                    boolean canGrief = livingEntity instanceof Player || ForgeEventFactory.getMobGriefingEvent(level, livingEntity);
                    if (isObsidian && canGrief) {
                        level.destroyBlock(blockPos, false);
                    }
                    mjolnirHit(livingEntity, level, blockPos, null);
                    level.explode(null, this.getDamageSource(), null, position.x, position.y, position.z, 2f, false,
                            canGrief ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE);
                    this.setDeltaMovement(prevMovement.scale(0.8f));
                } else {
                    this.setNoGravity(false);
                    super.onHitBlock(blockHitResult);
                    if (!this.hit) {
                        mjolnirHit(livingEntity, level, blockPos, null);
                    }
                }
            }
            this.hit = true;
        }
        this.setSoundEvent(DRSounds.MJOLNIR_IMPACT.get());
    }

    /** @param directTarget the entity that was hit, or {@code null} for a block impact */
    private void mjolnirHit(LivingEntity owner, Level level, BlockPos blockPos, @Nullable Entity directTarget) {
        // A living target gets its bolt from the area strike below, like everything around it, not a second one here.
        if (!(directTarget instanceof LivingEntity) && (directTarget == null || this.areaStruckEntityIds.add(directTarget.getId()))) {
            ThrownMjolnir.spawnLightning(level, blockPos, owner);
        }
        onHitLighnting(level, this, blockPos, owner, this.getDamageSource(), STRIKE_DAMAGE, STRIKE_FORCE, STRIKE_RADIUS, this.areaStruckEntityIds);
        if (owner instanceof Player player) {
            player.getCooldowns().addCooldown(this.mjolnirItem.getItem(), this.COOLDOWN);
        }
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    public boolean isChanneling() {
        return EnchantmentHelper.hasChanneling(this.mjolnirItem);
    }

    protected boolean tryPickup(Player player) {
        if (super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem())) {
            player.getCapability(MjolnirBindingProvider.MJOLNIR_BINDING).ifPresent(binding -> {
                binding.removeMjolnir(this.getUUID().toString());
            });
            return true;
        }
        return false;
//        return super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
    }

    public void shootMjolnirAtTarget(LivingEntity shooter, LivingEntity target) {
        this.shootMjolnirAt(shooter, target.position().add(0, target.getEyeHeight() / 1.5f, 0));
    }

    public void shootMjolnirAt(LivingEntity shooter, Vec3 targetPos) {
        Vec3 startPos = shooter.position().add(0, shooter.getEyeHeight() / 1.5f, 0);

        Vec3 direction = targetPos.subtract(startPos).normalize();

        this.setPos(startPos.x, startPos.y, startPos.z);
        this.shoot(direction.x, direction.y, direction.z, 3.8F, 0);
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return DRSounds.MJOLNIR_IMPACT.get();
    }

    public void playerTouch(Player p_37580_) {
        if (this.ownedBy(p_37580_) || this.getOwner() == null) {
            super.playerTouch(p_37580_);
        }
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Mjolnir", 10)) {
            this.mjolnirItem = ItemStack.of(tag.getCompound("Mjolnir"));
        }

        this.dealtDamage = tag.getBoolean("DealtDamage");
        this.hit = tag.getBoolean("thisHit");
        this.setReturning(tag.getBoolean("Returning"));
        this.thrownByThor = tag.getBoolean("ThrownByThor");
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Mjolnir", this.mjolnirItem.save(new CompoundTag()));
        tag.putBoolean("DealtDamage", this.dealtDamage);
        tag.putBoolean("thisHit", this.hit);
        tag.putBoolean("Returning", this.isReturning());
        tag.putBoolean("ThrownByThor", this.thrownByThor);
    }

    public void tickDespawn() {
        if (this.pickup != Pickup.ALLOWED) {
            super.tickDespawn();
        }

    }

    protected float getWaterInertia() {
        return 0.99F;
    }

    public boolean shouldRender(double p_37588_, double p_37589_, double p_37590_) {
        return true;
    }

    static {
        ID_FOIL = SynchedEntityData.defineId(ThrownMjolnir.class, EntityDataSerializers.BOOLEAN);
        SHOULD_RETURN = SynchedEntityData.defineId(ThrownMjolnir.class, EntityDataSerializers.BOOLEAN);
    }

    public static ThrownMjolnir create(EntityType<? extends ThrownMjolnir> type, Level level) {
        return new ThrownMjolnir(type, level);
    }

    public boolean hasHit() {
        return this.hit;
    }
}
