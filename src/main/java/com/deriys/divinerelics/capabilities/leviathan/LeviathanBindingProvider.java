package com.deriys.divinerelics.capabilities.leviathan;

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

public class LeviathanBindingProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<LeviathanBinding> LEVIATHAN_BINDING = CapabilityManager.get(new CapabilityToken<LeviathanBinding>() { });

    private LeviathanBinding leviathanBind = null;
    private final LazyOptional<LeviathanBinding> optional = LazyOptional.of(this::createLeviathanBinding);

    private LeviathanBinding createLeviathanBinding() {
        if(this.leviathanBind == null) {
            this.leviathanBind = new LeviathanBinding();
        }

        return this.leviathanBind;
    }
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == LEVIATHAN_BINDING) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createLeviathanBinding().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createLeviathanBinding().loadNBTData(nbt);
    }
}
