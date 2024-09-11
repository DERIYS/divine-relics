package com.deriys.divinerelics.entities.ai.thor;

import com.deriys.divinerelics.entities.entity.ThorEntity;

import static com.deriys.divinerelics.items.DraupnirSpear.RAND;

public enum ThorAttackState {
    NONE,
    MELEE_ATTACK,
    CLAP_ATTACK,
    GROUND_ATTACK,
    MJOLNIR_THROW;

    public static ThorAttackState getState(byte i) {
        return ThorAttackState.values()[i];
    }

    public static int getAttackStateTicks(ThorAttackState state) {
        return switch (state) {
            case CLAP_ATTACK -> 24;
            case GROUND_ATTACK -> 25;
            case MJOLNIR_THROW -> 15;
            default -> 12;
        };
    }

    public static ThorAttackState genCloseState(ThorAttackState lastAttack, ThorEntity thor) {
        float rand = RAND.nextFloat();
        if (rand < 0.3 && !lastAttack.equals(CLAP_ATTACK)) {
            return CLAP_ATTACK;
        } else if (!thor.waitsForMjolnir){
            return MELEE_ATTACK;
        } else {
            return CLAP_ATTACK;
        }
    }
}
