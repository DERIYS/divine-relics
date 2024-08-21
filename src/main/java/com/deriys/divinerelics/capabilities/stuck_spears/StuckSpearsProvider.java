package com.deriys.divinerelics.capabilities.stuck_spears;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StuckSpearsProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<StuckSpears> STUCK_SPEARS = CapabilityManager.get(new CapabilityToken<StuckSpears>() { });

    private StuckSpears spearBind = null;
    private final LazyOptional<StuckSpears> optional = LazyOptional.of(this::createStuckSpears);

    private StuckSpears createStuckSpears() {
        if(this.spearBind == null) {
            this.spearBind = new StuckSpears();
        }

        return this.spearBind;
    }
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == STUCK_SPEARS) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createStuckSpears().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createStuckSpears().loadNBTData(nbt);
    }
}
