package com.deriys.divinerelics.entities.ai.thor;

import com.deriys.divinerelics.entities.entity.ThorEntity;
import com.deriys.divinerelics.entities.entity.ThrownMjolnir;
import com.deriys.divinerelics.init.DRItems;
import com.deriys.divinerelics.init.DRSounds;
import com.deriys.divinerelics.items.Motosignir;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.deriys.divinerelics.entities.entity.ThrownMjolnir.onHitLighnting;
import static com.deriys.divinerelics.items.DraupnirSpear.RAND;

public class ThorAttackGoal extends MeleeAttackGoal {
    private final ThorEntity entity;
    private int attackDelay = -1;
    private int ticksUntilNextAttack = -1;
    private boolean shouldCountTillNextAttack = false;
    private ThorAttackState lastAttack = ThorAttackState.NONE;


    public ThorAttackGoal(PathfinderMob pathfinderMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pathfinderMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        entity = ((ThorEntity) pathfinderMob);
    }

    @Override
    public void start() {
        super.start();
//        assignDelayTicks();
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
        if (isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {
            shouldCountTillNextAttack = true;
            if(isTimeToStartAttackAnimation()) {
                entity.setAttacking(true);
                entity.playAttackSound(entity.getAttackState());
            }

            if(isTimeToAttack()) {
                this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getEyeY(), pEnemy.getZ());
                performAttack(pEnemy);
            }
        } else {
            if (entity.getAttackState() != ThorAttackState.NONE) {
                entity.setAttackState(ThorAttackState.NONE);
            }
        }
    }


    private boolean isEnemyWithinAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
        float attackRange = 2.8f;
        ThorEntity thor = this.entity;
        boolean isAttacking = thor.isAttacking();
        double distance = getAttackReachSqr(pEnemy) + attackRange * attackRange;
        if (pDistToEnemySqr <= distance) {
            if (!isAttacking && ticksUntilNextAttack <= 0) {
                ThorAttackState randAttack = ThorAttackState.genCloseState(thor, this.lastAttack);
                setNewAttackState(randAttack);
            }
            return true;
        } else {
            ThorAttackState attackState = thor.getAttackState();
            if (pDistToEnemySqr <= distance * 2f && (shouldClapAttack() || attackState == ThorAttackState.CLAP_ATTACK)) {
                if (!isAttacking && canClapAttack() && ticksUntilNextAttack <= 0) {
                    setNewAttackState(ThorAttackState.CLAP_ATTACK);
                }
                return true;
            } else if (pDistToEnemySqr <= distance * 5f && (shouldGroundAttack() || attackState == ThorAttackState.GROUND_ATTACK)) {
                if (!isAttacking && canGroundAttack() && ticksUntilNextAttack <= 0) {
                    setNewAttackState(ThorAttackState.GROUND_ATTACK);
                }
                return true;
            } else if (pDistToEnemySqr > distance * 5f && entity.canSeeTarget(pEnemy) && (shouldMjolnirThrow() || attackState == ThorAttackState.MJOLNIR_THROW)) {
                if (!isAttacking && !thor.waitsForMjolnir() && thor.thrownMjolnirUUID == null && ticksUntilNextAttack <= 0) {
                    setNewAttackState(ThorAttackState.MJOLNIR_THROW);
                }
                return true;
            } else return attackState == ThorAttackState.MJOLNIR_THROW && isAttacking;
        }
    }

    private boolean shouldMjolnirThrow() {
        return RAND.nextDouble() < 0.04D;
    }

    private boolean shouldClapAttack() {
        return RAND.nextDouble() < 0.078D;
    }

    private boolean shouldGroundAttack() {
        return RAND.nextDouble() < 0.04D;
    }

    private boolean canGroundAttack() {
        return this.lastAttack != ThorAttackState.GROUND_ATTACK;
    }

    private boolean canClapAttack() {
        return this.lastAttack != ThorAttackState.CLAP_ATTACK;
    }

    private void setNewAttackState(ThorAttackState attackState) {
        this.lastAttack = attackState;
        this.entity.setAttackState(attackState);
        this.assignDelayTicks();
    }

    private void assignDelayTicks() {
        int attackTicks = ThorAttackState.getAttackStateTicks(entity.getAttackState());
        ticksUntilNextAttack = attackTicks * 2;
        attackDelay = attackTicks;
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(attackDelay * 2);
    }

    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack == this.attackDelay;
    }

    protected boolean isTimeToStartAttackAnimation() {
        return this.ticksUntilNextAttack == this.attackDelay * 2;
    }

    protected int getTicksUntilNextAttack() {
        return this.ticksUntilNextAttack;
    }


    protected void performAttack(LivingEntity pEnemy) {
        ThorAttackState attackState = this.entity.getAttackState();
        if (attackState != ThorAttackState.MJOLNIR_THROW) {
            if (attackState == ThorAttackState.LEG_ATTACK) {
                Motosignir.applyKnockBack(pEnemy, this.entity, 1.8f);
            }
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(pEnemy);
        } else {
            ThorEntity thor = this.entity;
            thor.setHasMjolnirInHands(false);
            thor.setWaitsForMjolnir(true);
            Level level = thor.level;
            ThrownMjolnir thrownMjolnir = new ThrownMjolnir(level, thor, new ItemStack(DRItems.MJOLNIR.get()));
            thrownMjolnir.shootMjolnirAtTarget(thor, pEnemy);
            thrownMjolnir.pickup = AbstractArrow.Pickup.DISALLOWED;
            thrownMjolnir.setNoGravity(true);
            level.addFreshEntity(thrownMjolnir);
            thor.thrownMjolnirUUID = thrownMjolnir.getUUID();
            level.playSound(null, thor.getOnPos(), DRSounds.MJOLNIR_THROWING.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
        }
    }

    @Override
    public void tick() {
        super.tick();

        ThorEntity thor = this.entity;
        ThorAttackState state = thor.getAttackState();
        if (performsAttack(thor, 24, ThorAttackState.CLAP_ATTACK)) {
            onHitLighnting(thor.level, thor, thor.getOnPos(), thor, thor.thorDamageSource, 7f, 3.5f, 6);
        } else if (performsAttack(thor, 25, ThorAttackState.GROUND_ATTACK)) {
            onHitLighnting(thor.level, thor, thor.getOnPos(), thor, thor.thorDamageSource, 17f, 5f, 8);
        }

        if(!thor.level.isClientSide && shouldCountTillNextAttack && this.ticksUntilNextAttack > 0) {
            this.ticksUntilNextAttack--;
            if (this.ticksUntilNextAttack == 0){
                thor.setAttacking(false);
                if (state == ThorAttackState.MJOLNIR_THROW) {
                    thor.setAttackState(ThorAttackState.NONE);
                }
            }
        }
    }

    private boolean performsAttack(ThorEntity thor, int x, ThorAttackState attackState) {
        ThorAttackState thorAttackState = thor.getAttackState();
        return !thor.level.isClientSide && this.ticksUntilNextAttack == x && (thorAttackState == attackState || (lastAttack == attackState && thorAttackState == ThorAttackState.NONE));
    }

    @Override
    public void stop() {
        entity.setAttacking(false);
        entity.setAttackState(ThorAttackState.NONE); // to prevent mjolnir throw spamming
        super.stop();
    }
}
