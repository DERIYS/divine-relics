package com.deriys.divinerelics.entities.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

/**
 * Server-side progress of a timed attack: a wind-up, the hit, then the recovery, together lasting exactly as long as
 * its animation.
 * <p>
 * The attacking mob advances it every tick itself, so a started attack always plays out (hit included, in step with the
 * animation clients play from its start time) even if the attack goal stops because the target is gone, died or
 * switched to creative, and never resumes half-way when a target shows up again.
 */
public final class TimedAttack {
    /** Ticks since the attack started, or -1 without one. */
    private int tick = -1;
    @Nullable
    private LivingEntity target;
    @Nullable
    private Vec3 lastTargetPos;

    /** Squared distance within which a melee swing of {@code attacker} reaches {@code target}. */
    public static double meleeRangeSqr(Mob attacker, LivingEntity target, float extraRange) {
        double reach = attacker.getBbWidth() * 2.0F;
        return reach * reach + target.getBbWidth() + extraRange * extraRange;
    }

    public void start(@Nullable LivingEntity target) {
        this.tick = 0;
        this.target = target;
        this.lastTargetPos = target == null ? null : target.position();
    }

    /** Continues an attack that was saved {@code tick} ticks in (its target is not saved). */
    public void resume(int tick) {
        this.tick = tick;
        this.target = null;
        this.lastTargetPos = null;
    }

    public boolean isRunning() {
        return this.tick >= 0;
    }

    public int getTick() {
        return this.tick;
    }

    /** Whom the attack was aimed at, while it is still in the attacker's level. */
    @Nullable
    public LivingEntity getTarget() {
        return this.target;
    }

    /** Where the target was last seen (also after it is gone). */
    @Nullable
    public Vec3 getLastTargetPos() {
        return this.lastTargetPos;
    }

    /** Whether the target is still there, alive and within {@code rangeSqr} of {@code attacker}. */
    public boolean targetInReach(Mob attacker, double rangeSqr) {
        return this.target != null && this.target.isAlive() && attacker.distanceToSqr(this.target) <= rangeSqr;
    }

    /**
     * Advances the attack by one tick: runs {@code onImpact} at {@code impactTick} and ends it after {@code duration}
     * ticks.
     *
     * @return whether the attack ended with this tick
     */
    public boolean tick(Mob attacker, int impactTick, int duration, Runnable onImpact) {
        if (this.tick < 0) {
            return false;
        }
        if (this.target != null && (this.target.isRemoved() || this.target.level != attacker.level)) {
            this.target = null;
        }
        if (this.target != null) {
            this.lastTargetPos = this.target.position();
        }
        if (this.tick == impactTick) {
            onImpact.run();
        }
        if (++this.tick >= duration) {
            this.tick = -1;
            this.target = null;
            this.lastTargetPos = null;
            return true;
        }
        return false;
    }
}
