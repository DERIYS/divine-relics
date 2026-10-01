package com.deriys.divinerelics.entities.ai.thor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ThorKnockbackPhysicsTest {
    @ParameterizedTest
    @CsvSource({"3,0.2", "1.5,0.2", "5.5,0.2", "5,0.2", "10,1", "17,2.5", "15,2.5",
            "6,1", "5,1", "17,1", "28.9,2.5", "25.5,2.5", "30,0"})
    void launchTravelsConfiguredDistance(double distance, double height) {
        double lift = ThorKnockbackPhysics.launchSpeed(height);
        for (float friction : new float[]{0.6F, 0.8F, 0.98F}) {
            double speed = ThorKnockbackPhysics.horizontalSpeed(distance, friction, lift);
            assertEquals(distance, simulateTravel(speed, lift, friction, 0.0D), 0.03D);
            assertEquals(distance, simulateTravel(speed, lift, friction, Math.PI / 4.0D), 0.03D);
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0D, 0.2D, 1.0D, 2.5D, 5.0D})
    void launchReachesConfiguredHeight(double height) {
        double y = 0.0D;
        double lift = ThorKnockbackPhysics.launchSpeed(height);
        while (lift > 0.0D) {
            y += lift;
            lift = (lift - 0.08D) * 0.98D;
        }
        assertEquals(height, y, 1.0E-8D);
    }

    @Test
    void areaFalloffIsLinearAndNeverDropsBelowConfirmedFloors() {
        assertEquals(1.0D, ThorKnockbackPhysics.falloff(0.0D, 6.0D, 0.7D));
        assertEquals(0.85D, ThorKnockbackPhysics.falloff(3.0D, 6.0D, 0.7D));
        assertEquals(0.925D, ThorKnockbackPhysics.falloff(3.0D, 6.0D, 0.85D));
        assertEquals(0.7D, ThorKnockbackPhysics.falloff(6.0D, 6.0D, 0.7D), 1.0E-12D);
        assertEquals(0.85D, ThorKnockbackPhysics.falloff(6.0D, 6.0D, 0.85D), 1.0E-12D);
        for (double distance = 0.0D; distance <= 12.0D; distance += 0.1D) {
            assertTrue(ThorKnockbackPhysics.falloff(distance, 6.0D, 0.7D) >= 0.7D);
            assertTrue(ThorKnockbackPhysics.falloff(distance, 6.0D, 0.85D) >= 0.85D);
        }
    }

    @Test
    void proximityBonusIsContinuousAndLimitedTo170Percent() {
        assertEquals(1.7D, ThorKnockbackPhysics.proximityMultiplier(0.0D, 1.5D, 1.7D));
        assertEquals(1.35D, ThorKnockbackPhysics.proximityMultiplier(0.75D, 1.5D, 1.7D));
        assertEquals(1.0D, ThorKnockbackPhysics.proximityMultiplier(1.5D, 1.5D, 1.7D));
        assertEquals(1.0D, ThorKnockbackPhysics.proximityMultiplier(3.0D, 1.5D, 1.7D));
        assertEquals(1.0D, ThorKnockbackPhysics.proximityMultiplier(0.0D, 0.0D, 1.7D));
    }

    @Test
    void modelBoundaryRatherThanCenterReceivesMaximumBonus() {
        double contact = ThorKnockbackPhysics.surfaceDistance(-0.5D, 0, -0.5D, 0.5D, 2.8D, 0.5D,
                0.5D, 0, -0.3D, 1.1D, 1.8D, 0.3D);
        assertEquals(0.0D, contact);
        assertEquals(1.7D, ThorKnockbackPhysics.proximityMultiplier(contact, 1.5D, 1.7D));
        double gap = ThorKnockbackPhysics.surfaceDistance(-0.5D, 0, -0.5D, 0.5D, 2.8D, 0.5D,
                2.0D, 0, -0.3D, 2.6D, 1.8D, 0.3D);
        assertEquals(1.5D, gap);
        assertEquals(1.0D, ThorKnockbackPhysics.proximityMultiplier(gap, 1.5D, 1.7D));
    }

    @Test
    void surfaceDistanceIncludesVerticalAndDiagonalSeparation() {
        assertEquals(5.0D, ThorKnockbackPhysics.surfaceDistance(0, 0, 0, 1, 1, 1, 4, 0, 5, 5, 1, 6));
        assertEquals(2.0D, ThorKnockbackPhysics.surfaceDistance(0, 0, 0, 1, 1, 1, 0, 3, 0, 1, 4, 1));
        assertEquals(0.0D, ThorKnockbackPhysics.surfaceDistance(0, 0, 0, 2, 2, 2, 1, 1, 1, 3, 3, 3));
    }

    @Test
    void closeGroundSlamNeedsUnclampedMotion() {
        double lift = ThorKnockbackPhysics.launchSpeed(2.5D);
        double speed = ThorKnockbackPhysics.horizontalSpeed(17.0D * 1.7D, 0.6F, lift);
        assertTrue(speed > 3.9D);
        assertEquals(28.9D, simulateTravel(speed, lift, 0.6F, 0.0D), 0.03D);
    }

    @Test
    void disabledKnockbackAndZeroRadiusStayFinite() {
        assertEquals(0.0D, ThorKnockbackPhysics.horizontalSpeed(0.0D, 0.6F, 0.0D));
        assertEquals(1.0D, ThorKnockbackPhysics.falloff(0.0D, 0.0D, 0.7D));
    }

    private static double simulateTravel(double speed, double lift, float friction, double angle) {
        double x = 0, z = 0, y = 0;
        double dx = Math.cos(angle) * speed, dz = Math.sin(angle) * speed, dy = lift;
        boolean grounded = true;
        for (int tick = 0; tick < 1000; tick++) {
            if (Math.abs(dx) < 0.003D) dx = 0;
            if (Math.abs(dz) < 0.003D) dz = 0;
            if (Math.abs(dy) < 0.003D) dy = 0;
            if (dx == 0 && dz == 0) break;
            float drag = grounded ? friction * 0.91F : 0.91F;
            x += dx;
            z += dz;
            y += dy;
            grounded = y <= 0;
            if (grounded) {
                y = 0;
                dy = 0;
            }
            dx *= drag;
            dz *= drag;
            dy = (dy - 0.08D) * 0.98D;
        }
        return Math.hypot(x, z);
    }
}
