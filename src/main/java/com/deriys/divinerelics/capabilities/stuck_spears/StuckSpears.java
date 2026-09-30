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

    public void addSpear(Vec3 entry, Vec3 direction, float depth, float roll) {
        this.stuckSpears.add(new StuckSpear(entry.x, entry.y, entry.z,
                (float) direction.x, (float) direction.y, (float) direction.z, depth, roll));
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
                compound.putFloat("dirX", stuckSpear.dirX);
                compound.putFloat("dirY", stuckSpear.dirY);
                compound.putFloat("dirZ", stuckSpear.dirZ);
                compound.putFloat("depth", stuckSpear.depth);
                compound.putFloat("roll", stuckSpear.roll);
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
                this.stuckSpears.add(compound.contains("dirX") ? new StuckSpear(
                        compound.getDouble("x"),
                        compound.getDouble("y"),
                        compound.getDouble("z"),
                        compound.getFloat("dirX"),
                        compound.getFloat("dirY"),
                        compound.getFloat("dirZ"),
                        compound.getFloat("depth"),
                        compound.getFloat("roll")
                ) : legacySpear(compound));
            }
        }
    }

    /** Spears saved before the hit point was recorded only knew whether they hit the front or the back. */
    private static StuckSpear legacySpear(CompoundTag compound) {
        float intoBody = compound.getBoolean("isFront") ? -1.0F : 1.0F;
        return new StuckSpear(compound.getDouble("x"), compound.getDouble("y"), -0.3D * intoBody,
                0.0F, 0.0F, intoBody, 0.3F, 0.0F);
    }
}
