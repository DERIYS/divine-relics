package com.deriys.divinerelics.entities.entity;

import com.deriys.divinerelics.config.DivineRelicsCommonConfig;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.LookAtTradingPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
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

/**
 * Brok and Sindri: merchants and nothing else.
 * <p>
 * They are built on {@link AbstractVillager}, the merchant base shared with the wandering trader, not on
 * {@code Villager}. So they have no profession, brain, job site, bed or gossip, never breed, take no part in raids and
 * are never turned into zombie villagers or witches. What they keep is the trading: offers that unlock level by level
 * (Novice to Master) as they gain trading experience, and their own restock timer.
 */
public abstract class DwarfMerchantEntity extends AbstractVillager implements IAnimatable {
    private static final int RESTOCK_INTERVAL = DivineRelicsCommonConfig.BROK_AND_SINDRI_RESTOCK_TIME.get();
    /** Offers unlocked per level, as vanilla villagers do (every level of the dwarves has exactly this many). */
    private static final int OFFERS_PER_LEVEL = 2;
    /** Ticks after a level-up trade until the new offers appear, as for villagers (and only once trading ended). */
    private static final int LEVEL_UP_DELAY = 40;
    private static final byte HAPPY_PARTICLES_EVENT = 14;

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private final String walkAnimation;
    private final String idleAnimation;

    private int tradeLevel = 1;
    private int tradeXp;
    private int levelUpTimer;
    private boolean levelUpPending;
    private boolean tradedRecently;
    private long lastRestockTime = 0;
    private boolean needsRestock = false;

    protected DwarfMerchantEntity(EntityType<? extends DwarfMerchantEntity> entityType, Level level, String walkAnimation, String idleAnimation) {
        super(entityType, level);
        this.walkAnimation = walkAnimation;
        this.idleAnimation = idleAnimation;
    }

    /** This dwarf's trades: for each level (1 to 5), the offers it unlocks. */
    protected abstract Int2ObjectMap<VillagerTrades.ItemListing[]> getTradesByLevel();

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0D)
                .add(Attributes.FOLLOW_RANGE, 5.0D)
                .add(Attributes.MAX_HEALTH, 1000D)
                .add(Attributes.ATTACK_DAMAGE, 10D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new LookAtTradingPlayerGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new FloatGoal(this));
        // Brok and Sindri stand by each other.
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers(DwarfMerchantEntity.class));
    }

    // ---------------------------------------------------------------- trading

    public int getTradeLevel() {
        return this.tradeLevel;
    }

    @Override
    public int getVillagerXp() {
        return this.tradeXp;
    }

    @Override
    public boolean canRestock() {
        return true; // they restock on their own timer, see tick()
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(Items.VILLAGER_SPAWN_EGG) || !this.isAlive() || this.isTrading() || player.isSecondaryUseActive()) {
            return super.mobInteract(player, hand);
        }
        if (hand == InteractionHand.MAIN_HAND) {
            player.awardStat(Stats.TALKED_TO_VILLAGER);
        }
        if (!this.level.isClientSide && !this.getOffers().isEmpty()) {
            this.setTradingPlayer(player);
            this.openTradingScreen(player, this.getDisplayName(), this.tradeLevel);
        }
        return InteractionResult.sidedSuccess(this.level.isClientSide);
    }

    @Override
    protected void updateTrades() {
        VillagerTrades.ItemListing[] listings = this.getTradesByLevel().get(this.tradeLevel);
        if (listings != null) {
            this.addOffersFromItemListings(this.getOffers(), listings, OFFERS_PER_LEVEL);
        }
    }

    @Override
    protected void rewardTradeXp(MerchantOffer offer) {
        int playerXp = 3 + this.random.nextInt(4);
        this.tradeXp += offer.getXp();
        this.tradedRecently = true;
        if (VillagerData.canLevelUp(this.tradeLevel) && this.tradeXp >= VillagerData.getMaxXpPerLevel(this.tradeLevel)) {
            this.levelUpTimer = LEVEL_UP_DELAY;
            this.levelUpPending = true;
            playerXp += 5;
        }
        if (offer.shouldRewardExp()) {
            this.level.addFreshEntity(new ExperienceOrb(this.level, this.getX(), this.getY() + 0.5D, this.getZ(), playerXp));
        }
    }

    @Override
    protected void customServerAiStep() {
        if (!this.isTrading() && this.levelUpTimer > 0 && --this.levelUpTimer <= 0) {
            if (this.levelUpPending) {
                this.levelUpPending = false;
                this.tradeLevel++;
                this.updateTrades();
            }
            this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));
        }
        if (this.tradedRecently) {
            this.tradedRecently = false;
            this.level.broadcastEntityEvent(this, HAPPY_PARTICLES_EVENT);
        }
        super.customServerAiStep();
    }

    @Override
    public void handleEntityEvent(byte event) {
        if (event == HAPPY_PARTICLES_EVENT) {
            this.addParticlesAroundSelf(ParticleTypes.HAPPY_VILLAGER);
        } else {
            super.handleEntityEvent(event);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            long currentTime = this.level.getGameTime();
            if (this.needsRestock() && currentTime - this.lastRestockTime >= RESTOCK_INTERVAL) {
                this.restockTrades();
                this.lastRestockTime = currentTime;
            }
        }
    }

    private void restockTrades() {
        if (this.offers != null) {
            for (MerchantOffer offer : this.offers) {
                offer.resetUses();
            }
        }
    }

    private boolean needsRestock() {
        if (this.offers != null) {
            for (MerchantOffer offer : this.offers) {
                if (offer.isOutOfStock()) {
                    if (!this.needsRestock) {
                        this.lastRestockTime = this.level.getGameTime();
                        this.needsRestock = true;
                    }
                    return true;
                }
            }
        }
        this.needsRestock = false;
        return false;
    }

    // ---------------------------------------------------------------- saving

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("TradeLevel", this.tradeLevel);
        compound.putInt("Xp", this.tradeXp);
        compound.putLong("LastRestockTime", this.lastRestockTime);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("TradeLevel")) {
            this.tradeLevel = compound.getInt("TradeLevel");
        } else if (compound.contains("VillagerData", 10)) {
            // Saved while they were still villagers (older versions and the shop structure itself).
            this.tradeLevel = compound.getCompound("VillagerData").getInt("level");
        }
        this.tradeLevel = Math.max(1, Math.min(this.tradeLevel, VillagerData.MAX_VILLAGER_LEVEL));
        this.tradeXp = compound.getInt("Xp");
        if (compound.contains("LastRestockTime")) {
            this.lastRestockTime = compound.getLong("LastRestockTime");
        }
        if (this.offers != null) {
            // Villagers could save reputation discounts; the brothers never give any.
            for (MerchantOffer offer : this.offers) {
                offer.resetSpecialPriceDiff();
            }
        }
    }

    // ---------------------------------------------------------------- everything else a villager would do

    /** They stay at their shop: villagers never despawn, and neither do they now that they are no villagers. */
    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return null;
    }

    /** Players cannot hurt them; everything else can (and gets hit back). */
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof Player) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void thunderHit(ServerLevel level, LightningBolt lightning) {
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.GENERIC_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.GENERIC_DEATH;
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return null;
    }

    @Override
    protected SoundEvent getTradeUpdatedSound(boolean tradeAvailable) {
        return null;
    }

    // ---------------------------------------------------------------- animation

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        String animation = event.isMoving() ? this.walkAnimation : this.idleAnimation;
        event.getController().setAnimation(new AnimationBuilder().addAnimation(animation, ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
