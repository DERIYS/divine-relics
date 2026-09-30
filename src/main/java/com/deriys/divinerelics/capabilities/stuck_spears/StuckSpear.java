package com.deriys.divinerelics.capabilities.stuck_spears;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.world.phys.Vec3;

/**
 * A Draupnir spear stuck in an entity. Everything is stored in the entity's body frame (x = its left, y = up, z = the
 * way it faces, origin at its feet), so the spear turns along with the body.
 */
public class StuckSpear {
    /** Scale the spear model is drawn at. */
    public static final float RENDER_SCALE = 0.8F;
    /** Height of the tip in the thrown_draupnir_spear_item model (the blade points up), relative to the model's centre. */
    public static final float MODEL_TIP_Y = 20.0162F / 16.0F - 0.5F;
    /** Length of the drawn spear, from the butt (y = -0.25px in the model) to the tip. */
    public static final float LENGTH = (20.0162F + 0.25F) / 16.0F * RENDER_SCALE;
    /** How much of the spear always stays outside the hitbox, so it never disappears inside a large mob. */
    public static final float MIN_VISIBLE_LENGTH = 0.45F;
    /** Deepest the tip may go: never further than leaves {@link #MIN_VISIBLE_LENGTH} sticking out. */
    public static final float MAX_DEPTH = LENGTH - MIN_VISIBLE_LENGTH;
    /** Where particles come out of a stuck spear: its back end, as with spears landed in blocks. */
    public static final float PARTICLE_DISTANCE_FROM_TIP = 1.0F;

    /** Where the spear entered the entity. */
    public final double x;
    public final double y;
    public final double z;
    /** The spear's flight direction at the impact (a unit vector); the tip points this way. */
    public final float dirX;
    public final float dirY;
    public final float dirZ;
    /** How far the tip sits past the entry point, inside the entity. */
    public final float depth;
    /** Twist around the spear's own axis, in degrees. */
    public final float roll;

    public StuckSpear(double x, double y, double z, float dirX, float dirY, float dirZ, float depth, float roll) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dirX = dirX;
        this.dirY = dirY;
        this.dirZ = dirZ;
        this.depth = depth;
        this.roll = roll;
    }

    /** Where the model's centre goes (body frame) so that its tip ends up {@link #depth} past the entry point. */
    public Vec3 modelOrigin() {
        return this.pointFromTip(RENDER_SCALE * MODEL_TIP_Y);
    }

    /** The point {@code distance} back from the tip along the spear, in the body frame. */
    public Vec3 pointFromTip(double distance) {
        double along = this.depth - distance;
        return new Vec3(this.x + this.dirX * along, this.y + this.dirY * along, this.z + this.dirZ * along);
    }

    /** Converts a point of an entity's body frame (see this class) to world coordinates. */
    public static Vec3 bodyToWorld(Vec3 bodyPoint, Vec3 entityPosition, float bodyYawDegrees) {
        return bodyPoint.yRot(-bodyYawDegrees * ((float) Math.PI / 180.0F)).add(entityPosition);
    }

    /** Turns the model (tip up) to point along the flight direction, after twisting it around its own axis. */
    public Quaternion modelRotation() {
        Quaternion rotation;
        if (this.dirY > 0.9999F) {
            rotation = Quaternion.ONE.copy();
        } else if (this.dirY < -0.9999F) {
            rotation = Vector3f.XP.rotationDegrees(180.0F);
        } else {
            // Around (up x direction), by the angle between the two. Normalised here: Vector3f.normalize() is only an
            // approximation, which would slightly scale the model as well.
            float horizontal = (float) Math.sqrt(this.dirX * this.dirX + this.dirZ * this.dirZ);
            Vector3f axis = new Vector3f(this.dirZ / horizontal, 0.0F, -this.dirX / horizontal);
            rotation = axis.rotation((float) Math.acos(this.dirY));
        }
        rotation.mul(Vector3f.YP.rotationDegrees(this.roll));
        return rotation;
    }
}
