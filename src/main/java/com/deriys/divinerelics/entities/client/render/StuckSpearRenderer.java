package com.deriys.divinerelics.entities.client.render;

import com.deriys.divinerelics.capabilities.stuck_spears.StuckSpear;
import com.deriys.divinerelics.capabilities.stuck_spears.StuckSpears;
import com.deriys.divinerelics.capabilities.stuck_spears.StuckSpearsProvider;
import com.deriys.divinerelics.entities.entity.ThrownDraupnirSpear;
import com.deriys.divinerelics.init.DRItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Draws the Draupnir spears stuck in an entity and lets them give off the same particles as spears landed in blocks.
 * <p>
 * Vanilla renderers reach this through RenderLivingEvent; GeckoLib renderers never fire that event, so they call
 * {@link #render} themselves.
 */
@OnlyIn(Dist.CLIENT)
public final class StuckSpearRenderer {
    /** Landed spears send a particle every 4 ticks. */
    private static final int PARTICLE_INTERVAL = 4;
    /** Render-only stack, created once: creating an ItemStack per spear per frame also fired a capability event. */
    private static ItemStack thrownSpearStack;
    /** Entities that have spears stuck in them, for the particles (weak: forgotten once the entity is unloaded). */
    private static final Set<LivingEntity> WITH_SPEARS = Collections.newSetFromMap(new WeakHashMap<>());

    private StuckSpearRenderer() {
    }

    /** Draws the spears stuck in {@code entity}; {@code poseStack} must be at the entity's position (renderer origin). */
    public static void render(LivingEntity entity, PoseStack poseStack, MultiBufferSource buffer, int packedLight, float partialTick) {
        if (!entity.isAlive() || entity.isInvisible()) {
            return;
        }
        // Runs for every rendered living entity every frame: no capturing lambda, bail out early without spears.
        StuckSpears cap = entity.getCapability(StuckSpearsProvider.STUCK_SPEARS).orElse(null);
        if (cap == null || cap.getSpears().isEmpty()) {
            return;
        }
        if (thrownSpearStack == null) {
            thrownSpearStack = new ItemStack(DRItems.THROWN_DRAUPNIR_SPEAR.get());
        }
        float bodyYaw = Mth.rotLerp(partialTick, entity.yBodyRotO, entity.yBodyRot);

        for (StuckSpear stuckSpear : cap.getSpears()) {
            poseStack.pushPose();
            // Spears are recorded in the entity's body frame, so they turn along with the body.
            poseStack.mulPose(Vector3f.YP.rotationDegrees(-bodyYaw));
            // The model sits where the spear went in, along its flight direction, with the tip buried in the entity.
            Vec3 origin = stuckSpear.modelOrigin();
            poseStack.translate(origin.x, origin.y, origin.z);
            poseStack.mulPose(stuckSpear.modelRotation());
            poseStack.scale(StuckSpear.RENDER_SCALE, StuckSpear.RENDER_SCALE, StuckSpear.RENDER_SCALE);

            Minecraft.getInstance().getItemRenderer().renderStatic(entity, thrownSpearStack, ItemTransforms.TransformType.NONE,
                    false, poseStack, buffer, entity.level, packedLight, OverlayTexture.NO_OVERLAY, entity.getId());
            poseStack.popPose();
        }
    }

    /** Called whenever an entity's stuck spears change on this client. */
    public static void onSpearsChanged(LivingEntity entity, boolean hasSpears) {
        if (hasSpears) {
            WITH_SPEARS.add(entity);
        } else {
            WITH_SPEARS.remove(entity);
        }
    }

    /** Called once per client tick. */
    public static void tickParticles() {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            WITH_SPEARS.clear();
            return;
        }
        long gameTime = level.getGameTime();
        Iterator<LivingEntity> iterator = WITH_SPEARS.iterator();
        while (iterator.hasNext()) {
            LivingEntity entity = iterator.next();
            StuckSpears cap = entity.getCapability(StuckSpearsProvider.STUCK_SPEARS).orElse(null);
            if (entity.isRemoved() || entity.level != level || cap == null || cap.getSpears().isEmpty()) {
                iterator.remove();
                continue;
            }
            // Staggered per entity, so a crowd does not puff in unison.
            if ((gameTime + entity.getId()) % PARTICLE_INTERVAL != 0 || !entity.isAlive() || entity.isInvisible()) {
                continue;
            }
            for (StuckSpear stuckSpear : cap.getSpears()) {
                Vec3 end = StuckSpear.bodyToWorld(stuckSpear.pointFromTip(StuckSpear.PARTICLE_DISTANCE_FROM_TIP),
                        entity.position(), entity.yBodyRot);
                ThrownDraupnirSpear.spawnSpearParticle(level, end.x, end.y, end.z);
            }
        }
    }
}
