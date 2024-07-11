package com.deriys.divinerelics.capabilities.leviathan;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;

import java.util.ArrayList;
import java.util.List;

public class LeviathanBinding {
    private List<String> leviathanList = new ArrayList<>();

    public List<String> getLeviathanList() {
        return leviathanList;
    }

    public void addLeviathan(String leviathan) {
        this.leviathanList.add(leviathan);
    }

    public void removeLeviathan(String leviathan) {
        this.leviathanList.remove(leviathan);
    }

    public void copyFrom(LeviathanBinding source) {
        this.leviathanList = source.leviathanList;
    }

    public void saveNBTData(CompoundTag nbt) {
        if (!leviathanList.isEmpty()) {
            ListTag listTag = nbt.getList("leviathan_bind", 8);
            for (String mjolnir: this.leviathanList) {
                listTag.add(StringTag.valueOf(mjolnir));
            }
            nbt.put("leviathan_bind", listTag);
        }
    }

    public void loadNBTData(CompoundTag nbt) {
        if (nbt.contains("leviathan_bind")) {
            ListTag listTag = nbt.getList("leviathan_bind", 8);
            for (int i = 0; i < listTag.size(); i++) {
                String mjolnir = listTag.getString(i);
                this.leviathanList.add(mjolnir);
            }
        }
    }
}
