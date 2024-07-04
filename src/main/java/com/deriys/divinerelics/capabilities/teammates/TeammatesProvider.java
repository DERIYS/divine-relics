package com.deriys.divinerelics.capabilities.teammates;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TeammatesProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<Teammates> TEAMMATES = CapabilityManager.get(new CapabilityToken<Teammates>() { });

    private Teammates teammates = null;
    private final LazyOptional<Teammates> optional = LazyOptional.of(this::createTeammates);

    private Teammates createTeammates() {
        if(this.teammates == null) {
            this.teammates = new Teammates();
        }

        return this.teammates;
    }
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == TEAMMATES) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createTeammates().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createTeammates().loadNBTData(nbt);
    }

    public static boolean hasTeammate(LivingEntity player, LivingEntity livingEntity) {
        return player.getCapability(TeammatesProvider.TEAMMATES)
                .map(teammates -> teammates.getTeammates().contains(livingEntity.getUUID().toString()))
                .orElse(false);
    }
}
