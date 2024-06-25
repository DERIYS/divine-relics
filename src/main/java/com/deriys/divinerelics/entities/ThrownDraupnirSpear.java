package com.deriys.divinerelics.entities;

import com.deriys.divinerelics.core.networking.DRMessages;
import com.deriys.divinerelics.core.networking.packets.SpearParticleS2CPacket;
import com.deriys.divinerelics.items.DraupnirSpear;
import com.deriys.divinerelics.items.DRItems;
import com.deriys.divinerelics.sound.DRSounds;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class ThrownDraupnirSpear extends AbstractArrow {
    private static final EntityDataAccessor<Boolean> ID_FOIL;
    private double throwerX;
    private double throwerY;
    private double throwerZ;
    private ItemStack spearItem;
    private boolean dealtDamage;

    static {
        ID_FOIL = SynchedEntityData.defineId(ThrownDraupnirSpear.class, EntityDataSerializers.BOOLEAN);
    }

    public static ThrownDraupnirSpear create(EntityType<? extends ThrownDraupnirSpear> type, Level level) {
        return new ThrownDraupnirSpear(type, level);
    }
    public ThrownDraupnirSpear(EntityType<? extends ThrownDraupnirSpear> type, Level level) {
        super(type, level);
        this.spearItem = new ItemStack(DRItems.DRAUPNIR_SPEAR.get());
    }

    public ThrownDraupnirSpear(Level level, LivingEntity livingEntity, ItemStack itemStack) {
        super(DREntitiyTypes.THROWN_DRAUPNIR_SPEAR.get(), livingEntity, level);
        this.spearItem = itemStack;
        this.entityData.set(ID_FOIL, itemStack.hasFoil());
    }

    public void setThrowerPos(Vec3 throwerPos) {
        this.throwerX = throwerPos.x;
        this.throwerY = throwerPos.y;
        this.throwerZ = throwerPos.z;
    }

    public static void spawnSpearParticles(ClientLevel level, double x, double y, double z, double throwerX, double throwerY, double throwerZ, float pitch, float height) {
        BlockParticleOption particleOption = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.WHITE_CONCRETE.defaultBlockState());

        double pitchRadians = Math.toRadians(pitch);

        Vec3 throwVec = new Vec3(throwerX - x, throwerY - y, throwerZ - z).normalize().scale(height * Math.cos(pitchRadians));

        double offsetX = throwVec.x;
        double offsetY = -height * Math.sin(pitchRadians);
        double offsetZ = throwVec.z;

        level.addParticle(particleOption,
                x + offsetX + (Math.random() * 0.2 - 0.1),
                y + offsetY + (Math.random() * 0.2 - 0.1),
                z + offsetZ + (Math.random() * 0.2 - 0.1),
                0, 0, 0);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_FOIL, false);
    }

    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }
        if (!level.isClientSide && this.tickCount % 4 == 0) {
            SpearParticleS2CPacket packet = new SpearParticleS2CPacket(this.getX(), this.getY(), this.getZ(), this.throwerX, this.throwerY, this.throwerZ, this.getXRot(), 1F);
            DRMessages.sendToChunk(packet, level.getChunkAt(this.getOnPos()));
        }
        super.tick();
    }

    protected ItemStack getPickupItem() {
        return this.spearItem.copy();
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 p_37575_, Vec3 p_37576_) {
        return this.dealtDamage ? null : super.findHitEntity(p_37575_, p_37576_);
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    protected void onHitEntity(EntityHitResult hitResult) {
        Entity hurtEntity = hitResult.getEntity();
        float damage = 7.0F;
        if (hurtEntity instanceof LivingEntity livingEntity) {
            damage += EnchantmentHelper.getDamageBonus(this.spearItem, livingEntity.getMobType());
        }

        Entity owner = this.getOwner();
        DamageSource damageSource = DamageSource.trident(this, (Entity)(owner == null ? this : owner));
        this.dealtDamage = true;
        SoundEvent soundEvent = DRSounds.DRAUPNIR_SPEAR_LANDING.get();
        if (hurtEntity.hurt(damageSource, damage)) {
            if (hurtEntity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (hurtEntity instanceof LivingEntity livingEntityHurt && !(hurtEntity instanceof Player && ((Player) hurtEntity).isCreative())) {
                if (owner instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingEntityHurt, owner);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)owner, livingEntityHurt);
                }

                if (this.spearItem.getItem() instanceof DraupnirSpear draupnirSpear) {
                    draupnirSpear.addThrownSpear(this.spearItem, hurtEntity.getUUID());
                    if (!level.isClientSide) {
                        DraupnirSpear.sendExplosionPacket(this.level, hurtEntity.getX(), this.getY(), hurtEntity.getZ(), 1D, 0.5D, 10);
                    }
                }

                this.doPostHurtEffects(livingEntityHurt);
                this.discard();
            }
        }
        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.001, -0.01, -0.001));
        this.playSound(soundEvent, 2.0F, 1.0F);
    }

    @Override
    protected void onHitBlock(BlockHitResult p_36755_) {
        super.onHitBlock(p_36755_);
        this.setSoundEvent(DRSounds.DRAUPNIR_SPEAR_LANDING.get());
    }

    public boolean isFoil() {
        return (Boolean)this.entityData.get(ID_FOIL);
    }

    protected boolean tryPickup(Player p_150196_) {
        return super.tryPickup(p_150196_) || this.isNoPhysics() && this.ownedBy(p_150196_) && p_150196_.getInventory().add(this.getPickupItem());
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return DRSounds.DRAUPNIR_SPEAR_LANDING.get();
    }

    public void playerTouch(Player p_37580_) {
        if (this.ownedBy(p_37580_) || this.getOwner() == null) {
            super.playerTouch(p_37580_);
        }

    }

    public void readAdditionalSaveData(CompoundTag p_37578_) {
        super.readAdditionalSaveData(p_37578_);
        if (p_37578_.contains("Spear", 10)) {
            this.spearItem = ItemStack.of(p_37578_.getCompound("Spear"));
        }

        this.dealtDamage = p_37578_.getBoolean("DealtDamage");
    }

    public void addAdditionalSaveData(CompoundTag p_37582_) {
        super.addAdditionalSaveData(p_37582_);
        p_37582_.put("Spear", this.spearItem.save(new CompoundTag()));
        p_37582_.putBoolean("DealtDamage", this.dealtDamage);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void tickDespawn() {
        super.tickDespawn();
    }

    protected float getWaterInertia() {
        return 0.99F;
    }

    public boolean shouldRender(double p_37588_, double p_37589_, double p_37590_) {
        return true;
    }
}

