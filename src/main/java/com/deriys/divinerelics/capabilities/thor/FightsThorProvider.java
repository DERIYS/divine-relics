package com.deriys.divinerelics.capabilities.thor;

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

public class FightsThorProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<FightsThor> FIGHTS_THOR = CapabilityManager.get(new CapabilityToken<FightsThor>() { });

    private FightsThor fightsThor = null;
    private final LazyOptional<FightsThor> optional = LazyOptional.of(this::createFightsThor);

    private FightsThor createFightsThor() {
        if(this.fightsThor == null) {
            this.fightsThor = new FightsThor();
        }

        return this.fightsThor;
    }
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == FIGHTS_THOR) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createFightsThor().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createFightsThor().loadNBTData(nbt);
    }
}
