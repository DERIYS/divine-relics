package com.deriys.divinerelics.entities.entity;

import com.deriys.divinerelics.capabilities.leviathan.LeviathanBindingProvider;
import com.deriys.divinerelics.config.DivineRelicsCommonConfig;
import com.deriys.divinerelics.init.DREntitiyTypes;
import com.deriys.divinerelics.init.DRItems;
import com.deriys.divinerelics.init.DRSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

import static com.deriys.divinerelics.items.DraupnirSpear.RAND;

public class ThrownLeviathanAxe extends AbstractArrow {
    private static final EntityDataAccessor<Boolean> ID_FOIL;
    private static final EntityDataAccessor<Boolean> SHOULD_RETURN;
    private ItemStack leviathanItem;
    public boolean dealtDamage;
    public boolean isOnGround = false;
    public int rotationDirection = 1;
    public int clientSideReturnTridentTickCount;
    public boolean shouldReturn = false;
    public boolean relaxed = false;
    public DamageSource damageSource = DamageSource.trident(this, this.getOwner() == null ? this : this.getOwner());
    public final int COOLDOWN = DivineRelicsCommonConfig.LEVIATHAN_AXE_THROW_COOLDOWN.get();

    public ThrownLeviathanAxe(EntityType<? extends ThrownLeviathanAxe> entityType, Level level) {
        super(entityType, level);
        this.leviathanItem = new ItemStack(DRItems.LEVIATHAN_AXE.get());
    }

    public ThrownLeviathanAxe(Level p_37569_, LivingEntity p_37570_, ItemStack p_37571_) {
        super(DREntitiyTypes.THROWN_LEVIATHAN.get(), p_37570_, p_37569_);
        this.leviathanItem = new ItemStack(DRItems.LEVIATHAN_AXE.get());
        this.leviathanItem = p_37571_.copy();
        this.entityData.set(ID_FOIL, p_37571_.hasFoil());
        this.entityData.set(SHOULD_RETURN, this.shouldReturn);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_FOIL, false);
        this.entityData.define(SHOULD_RETURN, false);
    }

    public void tick() {

        if (this.inGroundTime > 0) {
            this.isOnGround = true;
            this.dealtDamage = true;
        }

        if (!this.level.isClientSide) {
            boolean properReturnOwner = this.isAcceptibleReturnOwner();
            if (properReturnOwner && !this.isReturning()) {
                if (this.position().distanceToSqr(this.getOwner().position()) > 10000) {
                    this.setReturning(true);
                    this.relaxed = false;
                    this.isOnGround = false;
                }
            }
        }

        if (this.getOwner() instanceof Player player) {
            if (!this.isAcceptibleReturnOwner() && !this.level.isClientSide && !this.relaxed && this.pickup == Pickup.ALLOWED) {
                this.pickup = Pickup.DISALLOWED;
                this.relax();
            } else if (!this.level.isClientSide && player.isAlive() && this.pickup == Pickup.DISALLOWED) {
                this.pickup = Pickup.ALLOWED;
            } else if (this.isReturning()) {
                this.relaxed = false;
                this.isOnGround = false;
                this.setNoPhysics(true);
                Vec3 vectorPM = player.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vectorPM.y * 0.015 * 4, this.getZ());
                if (this.level.isClientSide) {
                    this.yOld = this.getY();
                }

                double fact = 0.05 * 3;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add(vectorPM.normalize().scale(fact)));
                if (this.clientSideReturnTridentTickCount == 0) {
                    this.playSound(DRSounds.LEVIATHAN_AXE_RETURN.get(), 15.0F, 1.0F);
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
        this.relaxed = true;
        this.clientSideReturnTridentTickCount = 0;
        this.pickup = AbstractArrow.Pickup.DISALLOWED;
    }

    protected ItemStack getPickupItem() {
        return this.leviathanItem.copy();
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
        double damage = DivineRelicsCommonConfig.LEVIATHAN_AXE_DAMAGE.get();
        if (entity instanceof LivingEntity hitEntity) {
            damage += EnchantmentHelper.getDamageBonus(this.leviathanItem, hitEntity.getMobType());
        }

        Entity owner = this.getOwner();
        float volume = 1.0F;
        this.dealtDamage = true;
        this.rotationDirection *= -1;
        SoundEvent soundEvent = DRSounds.LEVIATHAN_AXE_IMPACT.get();
        if (entity.hurt(this.damageSource, (float) damage)) {
            if (owner instanceof Player player) {
                player.getCooldowns().addCooldown(this.leviathanItem.getItem(), this.COOLDOWN);
            }
            if (entity instanceof LivingEntity livingEntity && livingEntity.isAlive()) {
                livingEntity.setTicksFrozen(DivineRelicsCommonConfig.THROWN_LEVIATHAN_FROZEN_TICKS_HIT.get());
            }
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
        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
        this.playSound(soundEvent, volume, RAND.nextFloat() * 0.15F + 0.925F);
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        this.setSoundEvent(DRSounds.LEVIATHAN_AXE_IMPACT.get());
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    protected boolean tryPickup(Player player) {
        if (super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem())) {
            player.getCapability(LeviathanBindingProvider.LEVIATHAN_BINDING).ifPresent(binding -> {
                binding.removeLeviathan(this.getUUID().toString());
            });
            return true;
        }
        return false;
//        return super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return DRSounds.LEVIATHAN_AXE_IMPACT.get();
    }

    public void playerTouch(Player p_37580_) {
        if (this.ownedBy(p_37580_) || this.getOwner() == null) {
            super.playerTouch(p_37580_);
        }
    }

    public void readAdditionalSaveData(CompoundTag p_37578_) {
        super.readAdditionalSaveData(p_37578_);
        if (p_37578_.contains("Leviathan", 10)) {
            this.leviathanItem = ItemStack.of(p_37578_.getCompound("Leviathan"));
        }

        this.dealtDamage = p_37578_.getBoolean("DealtDamage");
        this.rotationDirection = p_37578_.getInt("RotationDirection");
    }

    public void addAdditionalSaveData(CompoundTag p_37582_) {
        super.addAdditionalSaveData(p_37582_);
        p_37582_.put("Leviathan", this.leviathanItem.save(new CompoundTag()));
        p_37582_.putBoolean("DealtDamage", this.dealtDamage);
        this.rotationDirection = p_37582_.getInt("RotationDirection");
    }

    public void tickDespawn() {
        if (this.pickup != AbstractArrow.Pickup.ALLOWED) {
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
        ID_FOIL = SynchedEntityData.defineId(ThrownLeviathanAxe.class, EntityDataSerializers.BOOLEAN);
        SHOULD_RETURN = SynchedEntityData.defineId(ThrownLeviathanAxe.class, EntityDataSerializers.BOOLEAN);
    }

    public static ThrownLeviathanAxe create(EntityType<? extends ThrownLeviathanAxe> type, Level level) {
        return new ThrownLeviathanAxe(type, level);
    }
}
