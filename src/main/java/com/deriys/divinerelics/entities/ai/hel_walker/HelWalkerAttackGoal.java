package com.deriys.divinerelics.entities.ai.hel_walker;

import com.deriys.divinerelics.entities.entity.HelWalkerEntity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.player.Player;

import static com.deriys.divinerelics.items.DraupnirSpear.RAND;

public class HelWalkerAttackGoal extends MeleeAttackGoal {
    private final HelWalkerEntity helWalker;
    private int attackDelay = 15;
    private int ticksUntilNextAttack = 30;
    private boolean shouldCountTillNextAttack = false;

    public HelWalkerAttackGoal(PathfinderMob pathfinderMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pathfinderMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        helWalker = ((HelWalkerEntity) pathfinderMob);
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
                helWalker.level.playSound(null, helWalker.getOnPos(), helWalker.getAttackSound(), SoundSource.HOSTILE, 1.0f, RAND.nextFloat() * 0.1F + 0.95F);
                helWalker.setAttacking(true);
            }

            if(isTimeToAttack()) {
                this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getEyeY(), pEnemy.getZ());
                performAttack(pEnemy);
            }
        } else {
            resetAttackCooldown();
            shouldCountTillNextAttack = false;
            helWalker.setAttacking(false);
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
        if (this.mob.doHurtTarget(pEnemy)) {
            if (pEnemy.isAlive()) {
                if (pEnemy instanceof Player player && !player.getAbilities().instabuild) {
                    pEnemy.setTicksFrozen(pEnemy.getTicksFrozen() + HelWalkerEntity.FREEZING_TIME);
                }
            }
        }
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
                helWalker.setAttacking(false);
                this.resetAttackCooldown();
            }
        }
    }

    @Override
    public void stop() {
        helWalker.setAttacking(false);
        super.stop();
    }
}
