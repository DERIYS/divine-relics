package com.deriys.divinerelics.entities.ai.thor;

import com.deriys.divinerelics.entities.entity.ThorEntity;

import static com.deriys.divinerelics.items.DraupnirSpear.RAND;

public enum ThorAttackState {
    NONE,
    MELEE_ATTACK,
    LEG_ATTACK,
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
            case MJOLNIR_THROW, LEG_ATTACK -> 15;
            default -> 12;
        };
    }

    public static ThorAttackState genCloseState(ThorEntity thor, ThorAttackState lastAttack) {
        float rand = RAND.nextFloat();
        if (rand < 0.2 && !lastAttack.equals(CLAP_ATTACK)) {
            return CLAP_ATTACK;
        } else if (!thor.waitsForMjolnir()){
            if (RAND.nextFloat() > 0.4) {
                return MELEE_ATTACK;
            } else {
                return LEG_ATTACK;
            }
        } else {
            return LEG_ATTACK;
        }
    }
}
