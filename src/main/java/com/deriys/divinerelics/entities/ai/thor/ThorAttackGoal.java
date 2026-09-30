package com.deriys.divinerelics.entities.ai.thor;

import com.deriys.divinerelics.config.DivineRelicsCommonConfig;
import com.deriys.divinerelics.entities.ai.TimedAttack;
import com.deriys.divinerelics.entities.entity.ThorEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

import javax.annotation.Nullable;

import static com.deriys.divinerelics.items.DraupnirSpear.RAND;

/**
 * Chases Thor's target and decides which attack he starts when. Once started, an attack is carried out by
 * {@link ThorEntity} itself, so it finishes (hit included, in time with its animation) even if this goal stops because
 * the target is gone.
 */
public class ThorAttackGoal extends MeleeAttackGoal {
    private final ThorEntity entity;
    private ThorAttackState lastAttack = ThorAttackState.NONE;

    public static double CLAP_ATTACK_DAMAGE = DivineRelicsCommonConfig.THOR_CLAP_DAMAGE.get();
    public static double CLAP_ATTACK_FORCE = DivineRelicsCommonConfig.THOR_CLAP_FORCE.get();
    public static double CLAP_ATTACK_RADUIS = DivineRelicsCommonConfig.THOR_CLAP_RADIUS.get();

    public static double GROUND_ATTACK_DAMAGE = DivineRelicsCommonConfig.THOR_GROUND_DAMAGE.get();
    public static double GROUND_ATTACK_FORCE = DivineRelicsCommonConfig.THOR_GROUND_FORCE.get();
    public static double GROUND_ATTACK_RADUIS = DivineRelicsCommonConfig.THOR_GROUND_RADIUS.get();

    public static final double LEG_ATTACK_FORCE = 1.8D;
    /** Extra reach of Thor's close attacks on top of the usual melee reach. */
    private static final float CLOSE_ATTACK_RANGE = 2.8F;

    public ThorAttackGoal(PathfinderMob pathfinderMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pathfinderMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        entity = ((ThorEntity) pathfinderMob);
    }

    /** Squared distance within which Thor's close attacks (melee, leg kick) reach {@code target}. */
    public static double attackRangeSqr(Mob thor, LivingEntity target) {
        return TimedAttack.meleeRangeSqr(thor, target, CLOSE_ATTACK_RANGE);
    }

    /** Starts an attack on {@code pEnemy} when Thor is free and it is in range of one. */
    @Override
    protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
        ThorEntity thor = this.entity;
        if (thor.isAttacking()) {
            return; // the running attack plays out on its own
        }
        double range = attackRangeSqr(thor, pEnemy);
        ThorAttackState attack = this.chooseAttack(pEnemy, pDistToEnemySqr, range);
        if (attack != null) {
            this.lastAttack = attack;
            thor.startAttack(attack, pEnemy);
        } else if (pDistToEnemySqr > range * 5.0D && thor.getAttackState() != ThorAttackState.NONE) {
            thor.setAttackState(ThorAttackState.NONE);
        }
    }

    @Nullable
    private ThorAttackState chooseAttack(LivingEntity enemy, double distSqr, double range) {
        ThorEntity thor = this.entity;
        if (distSqr <= range) {
            return ThorAttackState.genCloseState(thor, this.lastAttack);
        }
        if (distSqr <= range * 2.0D && shouldClapAttack() && this.lastAttack != ThorAttackState.CLAP_ATTACK) {
            return ThorAttackState.CLAP_ATTACK;
        }
        if (distSqr <= range * 5.0D && shouldGroundAttack() && this.lastAttack != ThorAttackState.GROUND_ATTACK) {
            return ThorAttackState.GROUND_ATTACK;
        }
        if (distSqr > range * 5.0D && shouldMjolnirThrow() && !thor.waitsForMjolnir() && thor.thrownMjolnirUUID == null
                && thor.canSeeTarget(enemy)) {
            return ThorAttackState.MJOLNIR_THROW;
        }
        return null;
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
}
