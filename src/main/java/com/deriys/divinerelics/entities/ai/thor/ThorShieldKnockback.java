package com.deriys.divinerelics.entities.ai.thor;

import com.deriys.divinerelics.config.DivineRelicsCommonConfig;
import com.deriys.divinerelics.core.networking.DRMessages;
import com.deriys.divinerelics.core.networking.packets.ThorKnockbackS2CPacket;
import com.deriys.divinerelics.items.GuardianShield;
import com.deriys.divinerelics.items.ShieldReactions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleConsumer;

/**
 * Blows of Thor and of a flying Mjölnir also reach whoever blocks them with a shield: no damage, but they are thrown
 * back several blocks. Guardian Shield parries reduce the launch speeds using the configured multiplier.
 * <p>
 * A fully blocked hit takes no damage, so vanilla never sends a player the new motion, and the knockback the attack
 * applied was lost.
 */
public final class ThorShieldKnockback {
    /** Highest speed per axis a motion packet can carry to a client (ClientboundSetEntityMotionPacket clamps it). */
    private static final double MAX_SYNCED_SPEED = 3.9D;
    /** The entity whose shield blocked the last hit (set from the ShieldBlockEvent, server thread only). */
    @Nullable
    private static LivingEntity lastShieldBlocker;
    private static boolean lastShieldWasParried;

    private ThorShieldKnockback() {
    }

    /** Called for every shield block that actually happens. */
    public static boolean onShieldBlock(LivingEntity blocker) {
        lastShieldBlocker = blocker;
        lastShieldWasParried = blocker instanceof Player player && GuardianShield.isParrying(player);
        return lastShieldWasParried;
    }

    public static double surfaceDistance(Entity source, LivingEntity target) {
        AABB a = source.getBoundingBox();
        AABB b = target.getBoundingBox();
        return ThorKnockbackPhysics.surfaceDistance(a.minX, a.minY, a.minZ, a.maxX, a.maxY, a.maxZ,
                b.minX, b.minY, b.minZ, b.maxX, b.maxY, b.maxZ);
    }

    public static double damageFactor(Entity source, LivingEntity target, double radius) {
        return ThorKnockbackPhysics.falloff(surfaceDistance(source, target), radius,
                DivineRelicsCommonConfig.THOR_AREA_MIN_DAMAGE_FACTOR.get());
    }

    private record Launch(double distance, double shieldDistance, double height) {
    }

    private static Launch launchFor(ThorAttackState attack) {
        return switch (attack) {
            case MELEE_ATTACK -> new Launch(DivineRelicsCommonConfig.THOR_MELEE_KNOCKBACK_DISTANCE.get(),
                    DivineRelicsCommonConfig.THOR_MELEE_SHIELD_KNOCKBACK_DISTANCE.get(),
                    DivineRelicsCommonConfig.THOR_MELEE_KNOCKBACK_HEIGHT.get());
            case LEG_ATTACK -> new Launch(DivineRelicsCommonConfig.THOR_LEG_KNOCKBACK_DISTANCE.get(),
                    DivineRelicsCommonConfig.THOR_LEG_SHIELD_KNOCKBACK_DISTANCE.get(),
                    DivineRelicsCommonConfig.THOR_LEG_KNOCKBACK_HEIGHT.get());
            case CLAP_ATTACK -> new Launch(DivineRelicsCommonConfig.THOR_CLAP_KNOCKBACK_DISTANCE.get(),
                    DivineRelicsCommonConfig.THOR_CLAP_SHIELD_KNOCKBACK_DISTANCE.get(),
                    DivineRelicsCommonConfig.THOR_CLAP_KNOCKBACK_HEIGHT.get());
            case GROUND_ATTACK -> new Launch(DivineRelicsCommonConfig.THOR_GROUND_KNOCKBACK_DISTANCE.get(),
                    DivineRelicsCommonConfig.THOR_GROUND_SHIELD_KNOCKBACK_DISTANCE.get(),
                    DivineRelicsCommonConfig.THOR_GROUND_KNOCKBACK_HEIGHT.get());
            case MJOLNIR_THROW -> new Launch(DivineRelicsCommonConfig.THOR_MJOLNIR_KNOCKBACK_DISTANCE.get(),
                    DivineRelicsCommonConfig.THOR_MJOLNIR_SHIELD_KNOCKBACK_DISTANCE.get(),
                    DivineRelicsCommonConfig.THOR_MJOLNIR_KNOCKBACK_HEIGHT.get());
            default -> throw new IllegalArgumentException("No knockback profile for " + attack);
        };
    }

    /** Applies a calibrated launch after hurt, replacing rather than stacking with vanilla damage knockback. */
    public static boolean hurtWithKnockback(LivingEntity target, Entity source, ThorAttackState attack, double radius,
                                            BooleanSupplier hurt) {
        Launch launch = launchFor(attack);
        Item shield = target.getUseItem().getItem();
        LivingEntity previousBlocker = lastShieldBlocker;
        boolean previousParry = lastShieldWasParried;
        lastShieldBlocker = null;
        lastShieldWasParried = false;
        boolean hurtTarget;
        boolean blocked;
        boolean parried;
        try {
            hurtTarget = hurt.getAsBoolean();
            blocked = lastShieldBlocker == target;
            parried = blocked && lastShieldWasParried;
        } finally {
            lastShieldBlocker = previousBlocker;
            lastShieldWasParried = previousParry;
        }
        if (hurtTarget || blocked) {
            double distance = blocked ? launch.shieldDistance : launch.distance;
            if (attack == ThorAttackState.CLAP_ATTACK || attack == ThorAttackState.GROUND_ATTACK) {
                double gap = surfaceDistance(source, target);
                distance *= ThorKnockbackPhysics.falloff(gap, radius,
                        DivineRelicsCommonConfig.THOR_AREA_MIN_KNOCKBACK_FACTOR.get());
                distance *= ThorKnockbackPhysics.proximityMultiplier(gap,
                        DivineRelicsCommonConfig.THOR_CLOSE_KNOCKBACK_RANGE.get(),
                        DivineRelicsCommonConfig.THOR_CLOSE_KNOCKBACK_MULTIPLIER.get());
            }
            double multiplier = parried ? DivineRelicsCommonConfig.THOR_PARRY_KNOCKBACK_MULTIPLIER.get() : 1.0D;
            launch(target, source.position(), distance, launch.height, multiplier);
            if (blocked && target instanceof Player player) {
                if (parried) {
                    ShieldReactions.releaseParry(player, shield);
                } else if (attack == ThorAttackState.GROUND_ATTACK) {
                    ShieldReactions.disableBlocking(player, shield);
                } else if (attack == ThorAttackState.MELEE_ATTACK || attack == ThorAttackState.LEG_ATTACK
                        || attack == ThorAttackState.CLAP_ATTACK) {
                    ShieldReactions.interruptBlocking(player);
                }
            }
        }
        return hurtTarget;
    }

    private static void launch(LivingEntity target, Vec3 from, double distance, double height, double multiplier) {
        Vec3 away = horizontalDirection(target, from);
        BlockPos below = new BlockPos(target.getX(), target.getBoundingBox().minY - 0.5000001D, target.getZ());
        float friction = target.level.getBlockState(below).getFriction(target.level, below, target);
        double lift = ThorKnockbackPhysics.launchSpeed(height);
        double speed = ThorKnockbackPhysics.horizontalSpeed(distance, friction, lift);
        target.setDeltaMovement(away.x * speed * multiplier, lift * multiplier, away.z * speed * multiplier);
        target.hasImpulse = true;
        if (target instanceof ServerPlayer player) {
            // Vanilla's motion packet clamps each axis to 3.9, losing the close-range slam's 170% launch.
            // Send the full motion directly, and suppress the later clamped packet to this player.
            target.hurtMarked = false;
            DRMessages.sendToPlayer(new ThorKnockbackS2CPacket(target.getDeltaMovement()), player);
        } else {
            target.hurtMarked = true;
        }
    }

    /**
     * Knocks {@code target} back with {@code force} and then runs the attack's {@code hurt}. If the target blocks the
     * hit with a shield, it is thrown back away from {@code from} instead, with reduced speeds after a parry.
     *
     * @param from      where the blow comes from
     * @param knockBack pushes the target away with the given strength (the attack's normal knockback, if any)
     * @return whether the target was hurt
     */
    public static boolean knockBackAndHurt(LivingEntity target, Vec3 from, double force, DoubleConsumer knockBack, BooleanSupplier hurt) {
        Vec3 motionBefore = target.getDeltaMovement();
        Item shield = target.getUseItem().getItem();
        knockBack.accept(force);
        LivingEntity previousBlocker = lastShieldBlocker;
        boolean previousParry = lastShieldWasParried;
        lastShieldBlocker = null;
        lastShieldWasParried = false;
        boolean hurtTarget;
        boolean blocked;
        boolean parried;
        try {
            hurtTarget = hurt.getAsBoolean();
            blocked = !hurtTarget && lastShieldBlocker == target;
            parried = blocked && lastShieldWasParried;
        } finally {
            lastShieldBlocker = previousBlocker;
            lastShieldWasParried = previousParry;
        }
        if (blocked) {
            target.setDeltaMovement(motionBefore); // the attack's own knockback never reached a blocking player
            throwBack(target, from);
            if (parried) {
                target.setDeltaMovement(target.getDeltaMovement().scale(DivineRelicsCommonConfig.THOR_PARRY_KNOCKBACK_MULTIPLIER.get()));
                if (target instanceof Player player) {
                    ShieldReactions.releaseParry(player, shield);
                }
            }
            target.hurtMarked = true; // sends the new motion to a player's client
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
        Vec3 away = horizontalDirection(target, from);
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
        return ThorKnockbackPhysics.launchSpeed(height);
    }

    private static Vec3 horizontalDirection(LivingEntity target, Vec3 from) {
        Vec3 away = new Vec3(target.getX() - from.x, 0.0D, target.getZ() - from.z);
        if (away.lengthSqr() < 1.0E-4D) {
            away = Vec3.directionFromRotation(0.0F, target.getYRot()).reverse();
        }
        return away.normalize();
    }

    /**
     * Blocks travelled per block/tick of horizontal start speed by an entity launched upwards at {@code lift} from level
     * ground of {@code friction}. Each tick it moves, then keeps (friction × 0.91) of its speed while on the ground
     * (including the launch tick) or 0.91 in the air.
     */
    static double distancePerSpeed(float friction, double lift) {
        return ThorKnockbackPhysics.travelDistance(1.0D, friction, lift);
    }
}
