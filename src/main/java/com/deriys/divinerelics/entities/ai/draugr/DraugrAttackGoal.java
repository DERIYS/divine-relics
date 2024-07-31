package com.deriys.divinerelics.entities.ai.draugr;

import com.deriys.divinerelics.entities.entity.DraugrEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class DraugrAttackGoal extends MeleeAttackGoal {
    private final DraugrEntity entity;
    private int attackDelay = 10;
    private int ticksUntilNextAttack = 10;
    private boolean shouldCountTillNextAttack = false;

    public DraugrAttackGoal(PathfinderMob pathfinderMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pathfinderMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        entity = ((DraugrEntity) pathfinderMob);
    }

    @Override
    public void start() {
        super.start();
        attackDelay = 10;
        ticksUntilNextAttack = 10;
        System.out.println("Starting attack goal");
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse();
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
        if (isEnemyWithinAttackDistance(pEnemy, pDistToEnemySqr)) {
            shouldCountTillNextAttack = true;
            if(isTimeToStartAttackAnimation()) {
                entity.setAttacking(true);
//                System.out.println("Starting attack animation");
            }

            if(isTimeToAttack()) {
                this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getEyeY(), pEnemy.getZ());
                performAttack(pEnemy);
//                System.out.println("Performing attack");
            }
        } else {
            resetAttackCooldown();
            shouldCountTillNextAttack = false;
            entity.setAttacking(false);
            entity.attackingTicks = 0;
//            System.out.println("Resetting attack cooldown");
        }
    }

    @Override
    protected double getAttackReachSqr(LivingEntity p_25556_) {
        return super.getAttackReachSqr(p_25556_) * 1.2f;
    }

    private boolean isEnemyWithinAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
        return pDistToEnemySqr <= this.getAttackReachSqr(pEnemy);
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(attackDelay * 2);
    }

    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }

    protected boolean isTimeToStartAttackAnimation() {
        return this.ticksUntilNextAttack <= this.attackDelay;
    }

    protected int getTicksUntilNextAttack() {
        return this.ticksUntilNextAttack;
    }


    protected void performAttack(LivingEntity pEnemy) {
        this.resetAttackCooldown();
        this.mob.swing(InteractionHand.MAIN_HAND);
        this.mob.doHurtTarget(pEnemy);
    }

    @Override
    public void tick() {
        super.tick();
        if(shouldCountTillNextAttack && this.ticksUntilNextAttack > 0) {
            this.ticksUntilNextAttack--;
        }
    }

    @Override
    public void stop() {
        entity.setAttacking(false);
        super.stop();
        System.out.println("Stopping attack goal");
    }
}
