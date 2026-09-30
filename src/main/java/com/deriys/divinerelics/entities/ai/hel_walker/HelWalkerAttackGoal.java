package com.deriys.divinerelics.entities.ai.hel_walker;

import com.deriys.divinerelics.entities.ai.TimedAttack;
import com.deriys.divinerelics.entities.entity.HelWalkerEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

/**
 * Chases the Hel Walker's target and starts an attack whenever the Hel Walker is free and the target is in reach. The
 * attack itself is carried out by {@link HelWalkerEntity}, so it finishes in time with its animation even if this goal
 * stops.
 */
public class HelWalkerAttackGoal extends MeleeAttackGoal {
    private final HelWalkerEntity helWalker;

    public HelWalkerAttackGoal(PathfinderMob pathfinderMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pathfinderMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        helWalker = ((HelWalkerEntity) pathfinderMob);
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
        if (!helWalker.isAttacking() && pDistToEnemySqr <= TimedAttack.meleeRangeSqr(helWalker, pEnemy, HelWalkerEntity.ATTACK_RANGE)) {
            helWalker.startAttack(pEnemy);
        }
    }
}
