package com.deriys.divinerelics.entities.ai.draugr;

import com.deriys.divinerelics.entities.ai.TimedAttack;
import com.deriys.divinerelics.entities.entity.DraugrEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

/**
 * Chases the Draugr's target and starts an attack whenever the Draugr is free and the target is in reach. The attack
 * itself is carried out by {@link DraugrEntity}, so it finishes in time with its animation even if this goal stops.
 */
public class DraugrAttackGoal extends MeleeAttackGoal {
    private final DraugrEntity draugr;

    public DraugrAttackGoal(PathfinderMob pathfinderMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pathfinderMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        draugr = ((DraugrEntity) pathfinderMob);
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
        if (!draugr.isAttacking() && pDistToEnemySqr <= TimedAttack.meleeRangeSqr(draugr, pEnemy, DraugrEntity.ATTACK_RANGE)) {
            draugr.startAttack(pEnemy);
        }
    }
}
