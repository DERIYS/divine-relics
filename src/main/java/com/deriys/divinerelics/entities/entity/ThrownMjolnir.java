package com.deriys.divinerelics.entities.entity;

import com.deriys.divinerelics.capabilities.mjolnir.MjolnirBindingProvider;
import com.deriys.divinerelics.init.DREntitiyTypes;
import com.deriys.divinerelics.init.DRItems;
import com.deriys.divinerelics.items.Motosignir;
import com.deriys.divinerelics.init.DRSounds;
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

import javax.annotation.Nullable;
import java.util.List;

import static com.deriys.divinerelics.capabilities.teammates.TeammatesProvider.hasTeammate;
import static com.deriys.divinerelics.items.HeimdallGauntlet.RAND;

public class ThrownMjolnir extends AbstractArrow {
    private static final EntityDataAccessor<Boolean> ID_FOIL;
    private static final EntityDataAccessor<Boolean> SHOULD_RETURN;
    public final float STRIKE_DAMAGE = 10f;
    private final int COOLDOWN = 141;
    private ItemStack mjolnirItem;
    private boolean dealtDamage;
    private boolean hit;
    public static int STRIKE_RADIUS = 5;
    public static float STRIKE_FORCE = 1.0f;
    public int clientSideReturnTridentTickCount;
    public boolean shouldReturn = false;
    public boolean relaxed = false;
    public DamageSource damageSource = DamageSource.trident(this, (Entity)(this.getOwner() == null ? this : this.getOwner()));

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
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_FOIL, false);
        this.entityData.define(SHOULD_RETURN, false);
    }

    public void tick() {
        if (!this.level.isClientSide && this.getOwner() instanceof ThorEntity thor && !thor.isAlive()) {
            this.discard();
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
                Vec3 vectorPM = owner.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vectorPM.y * 0.015 * 4, this.getZ());
                if (this.level.isClientSide) {
                    this.yOld = this.getY();
                }

                double fact = 0.05 * 3;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add(vectorPM.normalize().scale(fact)));
                if (this.clientSideReturnTridentTickCount == 0) {
                    this.playSound(DRSounds.MJOLNIR_RETURN.get(), 15.0F, 1.0F);
                }

                ++this.clientSideReturnTridentTickCount;
            }
        }
        super.tick();
    }

    public boolean isReturning() {
        return this.entityData.get(SHOULD_RETURN);
    }

    public void setReturning(boolean returning) {
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
        float damage = 25.0F;
        if (entity instanceof LivingEntity hitEntity) {
            damage += EnchantmentHelper.getDamageBonus(this.mjolnirItem, hitEntity.getMobType());
        }

        Entity owner = this.getOwner();
        float volume = 1.0F;
        this.dealtDamage = true;
        SoundEvent soundEvent = DRSounds.MJOLNIR_IMPACT.get();
        if (entity.hurt(this.damageSource, damage)) {
            this.hit = true;
            Level level = this.level;
            BlockPos blockPos = entity.blockPosition();
            if (!level.isClientSide && owner instanceof LivingEntity livingEntity) {
                mjolnirHit(livingEntity, level, blockPos);
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

    public static void onHitLighnting(Level level, Entity hammer, BlockPos blockPos, LivingEntity owner, DamageSource damageSource, float damage, float force, double strikeRadius) {

        double hammerX = hammer.getX();
        double hammerY = hammer.getY();
        double hammerZ = hammer.getZ();

        List<LivingEntity> entitiesInArea =
                Motosignir.getEntitiesInArea(level, hammerX, hammerY, hammerZ, strikeRadius);

        Motosignir.hurtAndKnockbackEntites(entitiesInArea, owner, hammer, damageSource, damage, force);

        int lightningCount = 0; // how many lightnings are spawned

        for (LivingEntity hitEntity: entitiesInArea) {
            if (hitEntity != owner && !hasTeammate(owner, hitEntity)) {
                spawnLightning(level, hitEntity.getOnPos(), owner);
                lightningCount++;
            }
        }
        if (lightningCount < 4) { // if there were < 4 lightnings, shoot at random position within 5 meter radius
            for (int i = lightningCount; i < 4; i++) {
                spawnLightning(level, getRandBlockPos(blockPos), owner);
            }
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
            Entity owner = this.getOwner();

            if (owner instanceof LivingEntity livingEntity) {
                if (!this.hit && !isBedrock) {
                    mjolnirHit(livingEntity, level, blockPos);
                    level.explode(null, this.damageSource, null, position.x, position.y, position.z, 2f, false, Explosion.BlockInteraction.DESTROY);
                    this.setDeltaMovement(prevMovement.scale(0.8f));
                } else {
                    this.setNoGravity(false);
                    super.onHitBlock(blockHitResult);
                    if (!this.hit) {
                        mjolnirHit(livingEntity, level, blockPos);
                    }
                }
            }
            this.hit = true;
        }
        this.setSoundEvent(DRSounds.MJOLNIR_IMPACT.get());
    }

    private void mjolnirHit(LivingEntity owner, Level level, BlockPos blockPos) {
        ThrownMjolnir.spawnLightning(level, blockPos, owner);
        onHitLighnting(level, this, blockPos, owner, this.damageSource, STRIKE_DAMAGE, STRIKE_FORCE, 5);
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
        Vec3 startPos = shooter.position().add(0, shooter.getEyeHeight() / 1.5f, 0);
        Vec3 targetPos = target.position().add(0, target.getEyeHeight() / 1.5f, 0);

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
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Mjolnir", this.mjolnirItem.save(new CompoundTag()));
        tag.putBoolean("DealtDamage", this.dealtDamage);
        tag.putBoolean("thisHit", this.hit);
        tag.putBoolean("Returning", this.isReturning());
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
