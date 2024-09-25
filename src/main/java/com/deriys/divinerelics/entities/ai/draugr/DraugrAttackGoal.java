package com.deriys.divinerelics.entities.ai.draugr;

import com.deriys.divinerelics.entities.entity.DraugrEntity;
import com.deriys.divinerelics.init.DRSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

import static com.deriys.divinerelics.items.DraupnirSpear.RAND;

public class DraugrAttackGoal extends MeleeAttackGoal {
    private final DraugrEntity entity;
    private int attackDelay = 15;
    private int ticksUntilNextAttack = 30;
    private boolean shouldCountTillNextAttack = false;

    public DraugrAttackGoal(PathfinderMob pathfinderMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pathfinderMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        entity = ((DraugrEntity) pathfinderMob);
    }

    @Override
    public void start() {
        super.start();
        attackDelay = 15;
        ticksUntilNextAttack = 30;
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
                entity.level.playSound(null, entity.getOnPos(), DRSounds.DRAUGR_ATTACK.get(), SoundSource.HOSTILE, 1.0f, RAND.nextFloat() * 0.1F + 0.95F);
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
        }
    }


    private boolean isEnemyWithinAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
        float attackRange = 2.8f;
        return pDistToEnemySqr <= getAttackReachSqr(pEnemy) + attackRange * attackRange;
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
        this.mob.swing(InteractionHand.MAIN_HAND);
        this.mob.doHurtTarget(pEnemy);
    }

    @Override
    public void tick() {
        super.tick();
//        if (!entity.level.isClientSide && this.ticksUntilNextAttack == 15) {
//            entity.level.playSound(null, entity.getOnPos(), DRSounds.DRAUGR_ATTACK.get(), SoundSource.HOSTILE, 1.0f, RAND.nextFloat() * 0.1F + 0.95F);
//        }
        if (shouldCountTillNextAttack && this.ticksUntilNextAttack > 0) {
            this.ticksUntilNextAttack--;
            if (this.ticksUntilNextAttack == 0) {
                entity.setAttacking(false);
                this.resetAttackCooldown();
            }
        }
    }

    @Override
    public void stop() {
        entity.setAttacking(false);
        super.stop();
    }
}
