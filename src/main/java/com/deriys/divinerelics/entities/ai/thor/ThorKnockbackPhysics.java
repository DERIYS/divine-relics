package com.deriys.divinerelics.entities.ai.thor;

final class ThorKnockbackPhysics {
    private static final double GRAVITY = 0.08D;
    private static final double VERTICAL_DRAG = 0.98D;
    private static final float AIR_FRICTION = 0.91F;
    private static final double STOP_SPEED = 0.003D;

    private ThorKnockbackPhysics() {
    }

    static double falloff(double distance, double radius, double minimum) {
        double fraction = radius > 0.0D ? Math.min(1.0D, Math.max(0.0D, distance / radius)) : 0.0D;
        return 1.0D - (1.0D - minimum) * fraction;
    }

    static double proximityMultiplier(double gap, double range, double maximum) {
        if (range <= 0.0D) {
            return 1.0D;
        }
        return 1.0D + (maximum - 1.0D) * (1.0D - Math.min(1.0D, Math.max(0.0D, gap / range)));
    }

    static double surfaceDistance(double minX, double minY, double minZ, double maxX, double maxY, double maxZ,
                                  double otherMinX, double otherMinY, double otherMinZ,
                                  double otherMaxX, double otherMaxY, double otherMaxZ) {
        double x = Math.max(0.0D, Math.max(minX - otherMaxX, otherMinX - maxX));
        double y = Math.max(0.0D, Math.max(minY - otherMaxY, otherMinY - maxY));
        double z = Math.max(0.0D, Math.max(minZ - otherMaxZ, otherMinZ - maxZ));
        return Math.sqrt(x * x + y * y + z * z);
    }

    static double launchSpeed(double height) {
        double low = 0.0D;
        double high = 3.9D;
        if (height <= 0.0D) {
            return 0.0D;
        }
        for (int i = 0; i < 40; i++) {
            double mid = (low + high) / 2.0D;
            if (apex(mid) < height) {
                low = mid;
            } else {
                high = mid;
            }
        }
        return high;
    }

    static double apex(double speed) {
        double height = 0.0D;
        while (speed > 0.0D) {
            height += speed;
            speed = (speed - GRAVITY) * VERTICAL_DRAG;
        }
        return height;
    }

    static double horizontalSpeed(double distance, float friction, double lift) {
        if (distance <= 0.0D) {
            return 0.0D;
        }
        double low = 0.0D;
        double high = distance;
        for (int i = 0; i < 40; i++) {
            double mid = (low + high) / 2.0D;
            if (travelDistance(mid, friction, lift) < distance) {
                low = mid;
            } else {
                high = mid;
            }
        }
        return high;
    }

    /** Vanilla moves first, using ground friction on the launch tick and air friction thereafter until landing. */
    static double travelDistance(double speed, float friction, double lift) {
        double distance = 0.0D;
        double height = 0.0D;
        double upwardSpeed = lift;
        boolean onGround = true;
        for (int tick = 0; tick < 1000 && speed >= STOP_SPEED; tick++) {
            float keep = onGround ? friction * AIR_FRICTION : AIR_FRICTION;
            distance += speed;
            height += upwardSpeed;
            onGround = height <= 0.0D;
            if (onGround) {
                height = 0.0D;
            }
            speed *= keep;
            upwardSpeed = onGround ? 0.0D : (upwardSpeed - GRAVITY) * VERTICAL_DRAG;
            if (Math.abs(upwardSpeed) < STOP_SPEED) {
                upwardSpeed = 0.0D;
            }
        }
        return distance;
    }
}
