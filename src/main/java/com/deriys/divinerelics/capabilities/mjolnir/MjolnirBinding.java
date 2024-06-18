package com.deriys.divinerelics.capabilities.mjolnir;

import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class MjolnirBinding {
    private UUID mjolnir = null;

    public UUID getMjolnir() {
        return mjolnir;
    }

    public void setMjolnir(UUID mjolnir) {
        this.mjolnir = mjolnir;
    }

    public void copyFrom(MjolnirBinding source) {
        this.mjolnir = source.mjolnir;
    }

    public void saveNBTData(CompoundTag nbt) {
        if (mjolnir != null) {
            nbt.putUUID("mjolnir_bind", mjolnir);
        }
    }
    public void loadNBTData(CompoundTag nbt) {
        if (nbt.hasUUID("mjolnir_bind")) {
            mjolnir = nbt.getUUID("mjolnir_bind");
        }
    }
}
