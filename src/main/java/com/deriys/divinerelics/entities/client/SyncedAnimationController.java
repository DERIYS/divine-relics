package com.deriys.divinerelics.entities.client;

import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;

/**
 * An animation controller that can start an animation part-way through.
 * <p>
 * GeckoLib only evaluates animations while the entity is being drawn, so an animation the server started while nobody
 * looked would begin late. {@link #playSynced} starts it at the point the server is already at instead.
 */
public class SyncedAnimationController<T extends IAnimatable> extends AnimationController<T> {
    /** Ticks to skip at the start of the next animation, or a negative value for none. */
    private double pendingSkipTicks = -1.0D;
    /** Server start time of the attack whose animation was started last. */
    private int playingStartTime = Integer.MIN_VALUE;

    public SyncedAnimationController(T animatable, String name, float transitionLengthTicks, IAnimationPredicate<T> predicate) {
        super(animatable, name, transitionLengthTicks, predicate);
    }

    /** Ticks (with the fraction of the current one) since server game time {@code startTime}, as seen by a client. */
    public static double ticksSince(Level level, int startTime, float partialTick) {
        return ((int) level.getGameTime() - startTime) + partialTick;
    }

    /**
     * Plays {@code animation} (once) for the attack the server started at game time {@code startTime}, from the point
     * {@code elapsedTicks} into it where the server is. Does nothing while that attack's animation is already playing.
     */
    public void playSynced(String animation, int startTime, double elapsedTicks) {
        if (startTime != this.playingStartTime) {
            this.playingStartTime = startTime;
            this.playFrom(new AnimationBuilder().addAnimation(animation, ILoopType.EDefaultLoopTypes.PLAY_ONCE), elapsedTicks);
        }
    }

    /** Plays {@code animation} from its start (even if it is already playing), already {@code ticksIn} ticks into it. */
    public void playFrom(AnimationBuilder animation, double ticksIn) {
        this.markNeedsReload();
        this.setAnimation(animation);
        this.pendingSkipTicks = Math.max(0.0D, ticksIn);
    }

    @Override
    protected double adjustTick(double tick) {
        // The animation starts running (its time is reset to 0) once the transition to it is over: start it later on.
        boolean startsRunning = this.shouldResetTick && this.getAnimationState() == AnimationState.Running;
        double adjusted = super.adjustTick(tick);
        if (startsRunning && this.pendingSkipTicks >= 0.0D) {
            this.tickOffset -= this.pendingSkipTicks / this.animationSpeed;
            this.pendingSkipTicks = -1.0D;
            return this.animationSpeed * (tick - this.tickOffset);
        }
        return adjusted;
    }
}
