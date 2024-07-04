package com.deriys.divinerelics.capabilities.teammates;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;

import java.util.ArrayList;
import java.util.List;

public class Teammates {
    private List<String> teammates = new ArrayList<>();

    public List<String> getTeammates() {
        return teammates;
    }

    public void addTeammate(String teammate) {
        this.teammates.add(teammate);
    }

    public void removeTeammate(String teammate) {
        this.teammates.remove(teammate);
    }

    public void copyFrom(Teammates source) {
        this.teammates = source.teammates;
    }

    public void saveNBTData(CompoundTag nbt) {
        if (!teammates.isEmpty()) {
            ListTag listTag = nbt.getList("DRTeammates", 8);
            for (String teammate: this.teammates) {
                listTag.add(StringTag.valueOf(teammate));
            }
            nbt.put("DRTeammates", listTag);
        }
    }

    public void loadNBTData(CompoundTag nbt) {
        if (nbt.contains("DRTeammates")) {
            ListTag listTag = nbt.getList("DRTeammates", 8);
            for (int i = 0; i < listTag.size(); i++) {
                String teammate = listTag.getString(i);
                this.teammates.add(teammate);
            }
        }
    }
}
