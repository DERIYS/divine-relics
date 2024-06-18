package com.deriys.divinerelics.capabilities.mjolnir;

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

public class MjolnirBindingProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<MjolnirBinding> MJOLNIR_BINDING = CapabilityManager.get(new CapabilityToken<MjolnirBinding>() { });

    private MjolnirBinding mjolnirBind = null;
    private final LazyOptional<MjolnirBinding> optional = LazyOptional.of(this::createMjolnirBinding);

    private MjolnirBinding createMjolnirBinding() {
        if(this.mjolnirBind == null) {
            this.mjolnirBind = new MjolnirBinding();
        }

        return this.mjolnirBind;
    }
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == MJOLNIR_BINDING) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createMjolnirBinding().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createMjolnirBinding().loadNBTData(nbt);
    }
}
