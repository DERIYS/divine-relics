package com.deriys.divinerelics.capabilities.stuck_spears;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class StuckSpears {
    private List<StuckSpear> stuckSpears = new ArrayList<>();

    public List<StuckSpear> getSpears() {
        return stuckSpears;
    }

    public void addSpear(Vec3 vec, float rot, boolean isFront) {
        this.stuckSpears.add(new StuckSpear(
                vec.x,
                vec.y,
                vec.z,
                rot,
                isFront
        ));
    }

    public void removeSpear() {
        if (!this.stuckSpears.isEmpty()) {
            this.stuckSpears.remove(0);
        }
    }

    public void setSpears(List<StuckSpear> stuckSpears) {
        this.stuckSpears = stuckSpears;
    }

    public void saveNBTData(CompoundTag nbt) {
        if (!stuckSpears.isEmpty()) {
            ListTag listTag = new ListTag();
            for (StuckSpear stuckSpear : this.stuckSpears) {
                CompoundTag compound = new CompoundTag();
                compound.putDouble("x", stuckSpear.x);
                compound.putDouble("y", stuckSpear.y);
                compound.putDouble("z", stuckSpear.z);
                compound.putFloat("rotation", stuckSpear.rotation);
                compound.putBoolean("isFront", stuckSpear.isFront);
                listTag.add(compound);
            }
            nbt.put("stuck_spears", listTag);
        }
    }

    public void loadNBTData(CompoundTag nbt) {
        if (nbt.contains("stuck_spears")) {
            ListTag listTag = nbt.getList("stuck_spears", 10); // 10 is the ID for CompoundTag
            this.stuckSpears = new ArrayList<>();
            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag compound = listTag.getCompound(i);
                this.stuckSpears.add(new StuckSpear(
                        compound.getDouble("x"),
                        compound.getDouble("y"),
                        compound.getDouble("z"),
                        compound.getFloat("rotation"),
                        compound.getBoolean("isFront")
                ));
            }
        }
    }
}
