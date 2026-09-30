package com.deriys.divinerelics.entities.ai.thor;

import com.deriys.divinerelics.entities.entity.ThorEntity;

import javax.annotation.Nullable;

import static com.deriys.divinerelics.items.DraupnirSpear.RAND;

public enum ThorAttackState {
    NONE(null, 0),
    MELEE_ATTACK("animation.thor.melee_attack", 24),
    LEG_ATTACK("animation.thor.leg_attack", 30),
    CLAP_ATTACK("animation.thor.clap_attack", 48),
    GROUND_ATTACK("animation.thor.ground_attack", 50),
    MJOLNIR_THROW("animation.thor.mjolnir_throw_attack", 30);

    /** values() clones the array on every call; this is read from synched data every tick and animation frame. */
    private static final ThorAttackState[] VALUES = values();

    @Nullable
    private final String animationName;
    private final int durationTicks;

    ThorAttackState(@Nullable String animationName, int durationTicks) {
        this.animationName = animationName;
        this.durationTicks = durationTicks;
    }

    public static ThorAttackState getState(byte i) {
        // Out-of-range ordinals (corrupted or foreign NBT) fall back to NONE instead of throwing while ticking.
        return i >= 0 && i < VALUES.length ? VALUES[i] : NONE;
    }

    /** The GeckoLib animation played for this attack. */
    @Nullable
    public String getAnimationName() {
        return this.animationName;
    }

    /** How long the attack lasts, in ticks: exactly the length of its animation. */
    public int getDurationTicks() {
        return this.durationTicks;
    }

    /** The tick of the attack (counted from its start) at which it hits: halfway through, where its animation strikes. */
    public int getImpactTick() {
        return this.durationTicks / 2;
    }

    public static ThorAttackState genCloseState(ThorEntity thor, ThorAttackState lastAttack) {
        float rand = RAND.nextFloat();
        if (rand < 0.2 && !lastAttack.equals(CLAP_ATTACK)) {
            return CLAP_ATTACK;
        } else if (!thor.waitsForMjolnir()){
            if (RAND.nextFloat() > 0.3) {
                return MELEE_ATTACK;
            } else {
                return LEG_ATTACK;
            }
        } else {
            return LEG_ATTACK;
        }
    }
}
