package com.deriys.divinerelics.entities.entity;

import com.deriys.divinerelics.init.DREntitiyTypes;
import com.deriys.divinerelics.init.DRItems;
import com.deriys.divinerelics.items.HeimdallGauntlet;
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

public class ThrownMjolnir extends AbstractArrow {
    private static final EntityDataAccessor<Boolean> ID_FOIL;
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
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_FOIL, false);
    }

    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        if (!this.level.isClientSide) {
            boolean properReturnOwner = this.isAcceptibleReturnOwner();
            if (properReturnOwner && !this.shouldReturn) {
                if (this.position().distanceToSqr(this.getOwner().position()) > 10000) {
                    System.out.println("returning 100");
                    this.shouldReturn = true;
                    this.relaxed = false;
                }
            }
        }

        if (this.getOwner() instanceof Player player) {
            if (player != null) {
//                System.out.printf("relaxed: %b\nshouldReturn: %b\nisAcceptibleReturnOwner: %b\npickupAllowed: %b\nplayerAlive: %b\n", this.relaxed, this.shouldReturn, this.isAcceptibleReturnOwner(), this.pickup == Pickup.ALLOWED, player.isAlive());
                if (!this.isAcceptibleReturnOwner() && !this.level.isClientSide && !this.relaxed && this.pickup == Pickup.ALLOWED) {
                    this.pickup = Pickup.DISALLOWED;
                    this.relax();
                } else if (!this.level.isClientSide && player.isAlive() && this.pickup == Pickup.DISALLOWED) {
                    this.pickup = Pickup.ALLOWED;
                } else if (this.shouldReturn) {
                    this.relaxed = false;
                    this.setNoPhysics(true);
                    Vec3 vectorPM = player.getEyePosition().subtract(this.position());
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
        }
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

    private void relax() {
        this.setNoGravity(false);
        this.setNoPhysics(false);
        this.setDeltaMovement(this.getDeltaMovement().multiply(0.001, 0.01, 0.001));
        this.shouldReturn = false;
        this.hit = true; // so that it won't spawn lightnings
        this.relaxed = true;
        this.clientSideReturnTridentTickCount = 0;
        this.pickup = Pickup.DISALLOWED;
    }

    protected ItemStack getPickupItem() {
        return this.mjolnirItem.copy();
    }

    public boolean isFoil() {
        return (Boolean)this.entityData.get(ID_FOIL);
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
        this.hit = true;
        SoundEvent soundEvent = DRSounds.MJOLNIR_IMPACT.get();
        if (entity.hurt(this.damageSource, damage)) {
            Level level = this.level;
            BlockPos blockPos = entity.blockPosition();
            if (!level.isClientSide && owner instanceof Player player) {
                mjolnirHit(player, level, blockPos);
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
        return new BlockPos(blockPos.getX() + HeimdallGauntlet.RAND.nextFloat() * 10 - 5, blockPos.getY(), blockPos.getZ() + HeimdallGauntlet.RAND.nextFloat() * 10 - 5);
    }

    public static void spawnLightning(Level level, BlockPos blockPos, Entity owner) {
        LightningBolt lightningBolt = (LightningBolt)EntityType.LIGHTNING_BOLT.create(level);
        lightningBolt.moveTo(Vec3.atBottomCenterOf(blockPos));
        lightningBolt.setCause(owner instanceof ServerPlayer ? (ServerPlayer)owner : null);
        level.addFreshEntity(lightningBolt);
    }

    public static void onHitLighnting(Level level, Entity hammer, BlockPos blockPos, LivingEntity owner, DamageSource damageSource, float damage, float force, int strikeRadius) {

        double hammerX = hammer.getX();
        double hammerY = hammer.getY();
        double hammerZ = hammer.getZ();

        List<LivingEntity> entitiesInArea =
                Motosignir.getEntitiesInArea(level, hammerX, hammerY, hammerZ, strikeRadius);

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

        Motosignir.hurtAndKnockbackEntites(entitiesInArea, owner, hammer, damageSource, damage, force);
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

            if (owner instanceof Player player) {
                if (!this.hit && !isBedrock) {
                    mjolnirHit(player, level, blockPos);
                    level.explode(null, this.damageSource, null, position.x, position.y, position.z, 2f, false, Explosion.BlockInteraction.DESTROY);
                    this.setDeltaMovement(prevMovement.scale(0.8f));
                } else {
                    this.setNoGravity(false);
                    super.onHitBlock(blockHitResult);
                    if (!this.hit) {
                        mjolnirHit(player, level, blockPos);
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

//        player.getCooldowns().addCooldown(this.mjolnirItem.getItem(), this.COOLDOWN);
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    public boolean isChanneling() {
        return EnchantmentHelper.hasChanneling(this.mjolnirItem);
    }

    protected boolean tryPickup(Player player) {
        return super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
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
