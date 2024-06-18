package com.deriys.divinerelics.entities;

import com.deriys.divinerelics.items.DRItems;
import com.deriys.divinerelics.items.HeimdallGauntlet;
import com.deriys.divinerelics.sound.DRSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class ThrownMjolnir extends AbstractArrow {
    private static final EntityDataAccessor<Boolean> ID_FOIL;
    private ItemStack mjolnirItem;
    private boolean dealtDamage;
    private boolean hit;
    public int clientSideReturnTridentTickCount;

    public ThrownMjolnir(EntityType<? extends ThrownMjolnir> entityType, Level level) {
        super(entityType, level);
        this.mjolnirItem = new ItemStack(DRItems.MJOLNIR.get());
    }

    public ThrownMjolnir(Level p_37569_, LivingEntity p_37570_, ItemStack p_37571_) {
        super(DREntitiyTypes.THROWN_MJOLNIR.get(), p_37570_, p_37569_);
        this.mjolnirItem = new ItemStack(DRItems.MJOLNIR.get());
        this.mjolnirItem = p_37571_.copy();
        this.entityData.set(ID_FOIL, p_37571_.hasFoil());
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_FOIL, false);
    }

    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }
//
//        Entity $$0 = this.getOwner();
//        int $$1 = (Byte)this.entityData.get(ID_LOYALTY);
//        if ($$1 > 0 && (this.dealtDamage || this.isNoPhysics()) && $$0 != null) {
//            if (!this.isAcceptibleReturnOwner()) {
//                if (!this.level.isClientSide && this.pickup == Pickup.ALLOWED) {
//                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
//                }
//
//                this.discard();
//            } else {
//                this.setNoPhysics(true);
//                Vec3 $$2 = $$0.getEyePosition().subtract(this.position());
//                this.setPosRaw(this.getX(), this.getY() + $$2.y * 0.015 * (double)$$1, this.getZ());
//                if (this.level.isClientSide) {
//                    this.yOld = this.getY();
//                }
//
//                double $$3 = 0.05 * (double)$$1;
//                this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add($$2.normalize().scale($$3)));
//                if (this.clientSideReturnTridentTickCount == 0) {
//                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
//                }
//
//                ++this.clientSideReturnTridentTickCount;
//            }
//        }
        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
        Entity $$0 = this.getOwner();
        if ($$0 != null && $$0.isAlive()) {
            return !($$0 instanceof ServerPlayer) || !$$0.isSpectator();
        } else {
            return false;
        }
    }

    protected ItemStack getPickupItem() {
        return this.mjolnirItem.copy();
    }

    public boolean isFoil() {
        return (Boolean)this.entityData.get(ID_FOIL);
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 p_37575_, Vec3 p_37576_) {
        return this.dealtDamage ? null : super.findHitEntity(p_37575_, p_37576_);
    }

    protected void onHitEntity(EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();
        float damage = 25.0F;
        if (entity instanceof LivingEntity hitEntity) {
            damage += EnchantmentHelper.getDamageBonus(this.mjolnirItem, hitEntity.getMobType());
        }

        Entity owner = this.getOwner();
        DamageSource damageSource = DamageSource.trident(this, (Entity)(owner == null ? this : owner));
        float volume = 1.0F;
        this.dealtDamage = true;
        this.hit = true;
        SoundEvent soundEvent = DRSounds.MJOLNIR_IMPACT.get();
        if (entity.hurt(damageSource, damage)) {
            Level level = this.level;
            BlockPos blockPos = entity.blockPosition();
            if (!level.isClientSide) {
                onHitLighnting(level, blockPos, this.getOwner());
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

        this.setDeltaMovement(this.getDeltaMovement().multiply(0, 0, 0));
        if (this.level instanceof ServerLevel && this.level.isThundering() && this.isChanneling()) {
            BlockPos blockPos = entity.blockPosition();
            if (this.level.canSeeSky(blockPos)) {
                LightningBolt lightningBolt = (LightningBolt)EntityType.LIGHTNING_BOLT.create(this.level);
                lightningBolt.moveTo(Vec3.atBottomCenterOf(blockPos));
                lightningBolt.setCause(owner instanceof ServerPlayer ? (ServerPlayer)owner : null);
                this.level.addFreshEntity(lightningBolt);
                soundEvent = SoundEvents.TRIDENT_THUNDER;
                volume = 5.0F;
            }
        }

        this.playSound(soundEvent, volume, 1.0F);
    }

    private BlockPos getRandBlockPos(BlockPos blockPos) {
        return new BlockPos(blockPos.getX() + HeimdallGauntlet.RAND.nextFloat() * 10 - 5, blockPos.getY(), blockPos.getZ() + HeimdallGauntlet.RAND.nextFloat() * 10 - 5);
    }

    protected void spawnLightning(Level level, BlockPos blockPos, Entity owner) {
        LightningBolt lightningBolt = (LightningBolt)EntityType.LIGHTNING_BOLT.create(level);
        lightningBolt.moveTo(Vec3.atBottomCenterOf(blockPos));
        lightningBolt.setCause(owner instanceof ServerPlayer ? (ServerPlayer)owner : null);
        level.addFreshEntity(lightningBolt);
    }

    protected void onHitLighnting(Level level, BlockPos blockPos, Entity owner) {
        spawnLightning(this.level, blockPos, owner);
        for (int i = 0; i < 3; i++) {
            spawnLightning(level, getRandBlockPos(blockPos), owner);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        Level level = this.level;
        BlockPos blockPos = blockHitResult.getBlockPos();
        if (!level.isClientSide && !this.hit) {
            Entity owner = this.getOwner();
            onHitLighnting(level, blockPos, owner);
            DamageSource damageSource = (owner instanceof LivingEntity) ? DamageSource.explosion((LivingEntity) owner) : null;
            level.explode(null, damageSource, null, blockPos.getX(), blockPos.getY() + 1, blockPos.getZ(), 1.5F, false, Explosion.BlockInteraction.BREAK);
            this.hit = true;
        }
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    public boolean isChanneling() {
        return EnchantmentHelper.hasChanneling(this.mjolnirItem);
    }

    protected boolean tryPickup(Player p_150196_) {
        if (super.tryPickup(p_150196_) || this.isNoPhysics() && this.ownedBy(p_150196_) && p_150196_.getInventory().add(this.getPickupItem())) {
            this.hit = false;
            return true;
        } return false;
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return DRSounds.MJOLNIR_IMPACT.get();
    }

    public void playerTouch(Player p_37580_) {
        if (this.ownedBy(p_37580_) || this.getOwner() == null) {
            super.playerTouch(p_37580_);
        }
    }

    public void readAdditionalSaveData(CompoundTag p_37578_) {
        super.readAdditionalSaveData(p_37578_);
        if (p_37578_.contains("Mjolnir", 10)) {
            this.mjolnirItem = ItemStack.of(p_37578_.getCompound("Mjolnir"));
        }

        this.dealtDamage = p_37578_.getBoolean("DealtDamage");
    }

    public void addAdditionalSaveData(CompoundTag p_37582_) {
        super.addAdditionalSaveData(p_37582_);
        p_37582_.put("Mjolnir", this.mjolnirItem.save(new CompoundTag()));
        p_37582_.putBoolean("DealtDamage", this.dealtDamage);
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
    }

    public static ThrownMjolnir create(EntityType<? extends ThrownMjolnir> type, Level level) {
        return new ThrownMjolnir(type, level);
    }
}
