package com.deriys.divinerelics.entities.ai.thor;

import com.deriys.divinerelics.config.DivineRelicsCommonConfig;
import com.deriys.divinerelics.items.GuardianShield;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleConsumer;

/**
 * Blows of Thor and of a flying Mjölnir also reach whoever blocks them with a shield: no damage, but they are thrown
 * back several blocks. Only a successful Guardian Shield parry stops it completely.
 * <p>
 * A fully blocked hit takes no damage, so vanilla never sends a player the new motion, and the knockback the attack
 * applied was lost.
 */
public final class ThorShieldKnockback {
    /** Highest speed per axis a motion packet can carry to a client (ClientboundSetEntityMotionPacket clamps it). */
    private static final double MAX_SYNCED_SPEED = 3.9D;
    /** Movement physics of LivingEntity.travel for an entity without movement input. */
    private static final double GRAVITY = 0.08D;
    private static final double VERTICAL_DRAG = 0.98D;
    private static final float AIR_FRICTION = 0.91F;
    private static final double STOP_SPEED = 0.003D;

    /** The entity whose shield blocked the last hit (set from the ShieldBlockEvent, server thread only). */
    @Nullable
    private static LivingEntity lastShieldBlocker;

    private ThorShieldKnockback() {
    }

    /** Called for every shield block that actually happens. */
    public static void onShieldBlock(LivingEntity blocker) {
        lastShieldBlocker = blocker;
    }

    /**
     * Knocks {@code target} back with {@code force} and then runs the attack's {@code hurt}. If the target blocks the
     * hit with a shield, it is thrown back away from {@code from} instead (not after a Guardian Shield parry).
     *
     * @param from      where the blow comes from
     * @param knockBack pushes the target away with the given strength (the attack's normal knockback, if any)
     * @return whether the target was hurt
     */
    public static boolean knockBackAndHurt(LivingEntity target, Vec3 from, double force, DoubleConsumer knockBack, BooleanSupplier hurt) {
        Vec3 motionBefore = target.getDeltaMovement();
        knockBack.accept(force);
        lastShieldBlocker = null;
        boolean hurtTarget = hurt.getAsBoolean();
        boolean blocked = !hurtTarget && lastShieldBlocker == target;
        lastShieldBlocker = null;
        if (blocked) {
            target.setDeltaMovement(motionBefore); // the attack's own knockback never reached a blocking player
            if (!(target instanceof Player player && GuardianShield.isParrying(player))) {
                throwBack(target, from);
                target.hurtMarked = true; // sends the new motion to a player's client
            }
        }
        return hurtTarget;
    }

    /**
     * Throws {@code target} away from {@code from}: lifted {@code Thor Shield Knockback Height} blocks, fast enough to
     * land and slide to a stop {@code Thor Shield Knockback Distance} blocks away on level ground.
     * <p>
     * On the ground an entity keeps only (friction × 0.91) of its speed per tick and stops at the first bump, so a slide
     * along it dies out after a few blocks. In the air it keeps 0.91 per tick, which is what makes the blow carry.
     */
    public static void throwBack(LivingEntity target, Vec3 from) {
        Vec3 away = new Vec3(target.getX() - from.x, 0.0D, target.getZ() - from.z);
        if (away.lengthSqr() < 1.0E-4D) {
            away = Vec3.directionFromRotation(0.0F, target.getYRot()).reverse(); // straight above/below: push backwards
        }
        away = away.normalize();
        BlockPos below = new BlockPos(target.getX(), target.getBoundingBox().minY - 0.5000001D, target.getZ());
        float friction = target.level.getBlockState(below).getFriction(target.level, below, target);
        double lift = launchSpeed(DivineRelicsCommonConfig.THOR_SHIELD_KNOCKBACK_HEIGHT.get());
        double speed = DivineRelicsCommonConfig.THOR_SHIELD_KNOCKBACK_DISTANCE.get() / distancePerSpeed(friction, lift);
        // Clients cap each axis: scale the whole push down instead, so it keeps its direction.
        speed = Math.min(speed, MAX_SYNCED_SPEED / Math.max(Math.abs(away.x), Math.abs(away.z)));
        target.setDeltaMovement(away.x * speed, Math.max(target.getDeltaMovement().y, lift), away.z * speed);
        target.hasImpulse = true;
    }

    /** Upward speed that lifts an entity {@code height} blocks at the top of its flight. */
    static double launchSpeed(double height) {
        if (height <= 0.0D) {
            return 0.0D;
        }
        double low = 0.0D;
        double high = MAX_SYNCED_SPEED;
        for (int i = 0; i < 30; i++) {
            double mid = (low + high) / 2.0D;
            if (apex(mid) < height) {
                low = mid;
            } else {
                high = mid;
            }
        }
        return high;
    }

    private static double apex(double upwardSpeed) {
        double height = 0.0D;
        while (upwardSpeed > 0.0D) {
            height += upwardSpeed;
            upwardSpeed = (upwardSpeed - GRAVITY) * VERTICAL_DRAG;
        }
        return height;
    }

    /**
     * Blocks travelled per block/tick of horizontal start speed by an entity launched upwards at {@code lift} from level
     * ground of {@code friction}. Each tick it moves, then keeps (friction × 0.91) of its speed while on the ground
     * (including the launch tick) or 0.91 in the air.
     */
    static double distancePerSpeed(float friction, double lift) {
        double distance = 0.0D;
        double speed = 1.0D;
        double height = 0.0D;
        double upwardSpeed = lift;
        boolean onGround = true;
        for (int tick = 0; tick < 1000 && speed >= STOP_SPEED; tick++) {
            float keep = onGround ? friction * 0.91F : AIR_FRICTION;
            distance += speed;
            if (upwardSpeed > 0.0D || !onGround) {
                height += upwardSpeed;
                onGround = height <= 0.0D;
            }
            speed *= keep;
            upwardSpeed = onGround ? 0.0D : (upwardSpeed - GRAVITY) * VERTICAL_DRAG;
        }
        return distance;
    }
}
