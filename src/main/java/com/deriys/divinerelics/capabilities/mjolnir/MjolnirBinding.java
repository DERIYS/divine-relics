package com.deriys.divinerelics.capabilities.mjolnir;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;

import java.util.ArrayList;
import java.util.List;

public class MjolnirBinding {
    private List<String> mjolnirList = new ArrayList<>();

    public List<String> getMjolnirList() {
        return mjolnirList;
    }

    public void addMjolnir(String mjolnir) {
        this.mjolnirList.add(mjolnir);
    }

    public void removeMjolnir(String mjolnir) {
        this.mjolnirList.remove(mjolnir);
    }

    public void copyFrom(MjolnirBinding source) {
        this.mjolnirList = source.mjolnirList;
    }

    public void saveNBTData(CompoundTag nbt) {
        if (!mjolnirList.isEmpty()) {
            ListTag listTag = nbt.getList("mjolnir_bind", 8);
            for (String mjolnir: this.mjolnirList) {
                listTag.add(StringTag.valueOf(mjolnir));
            }
            nbt.put("mjolnir_bind", listTag);
        }
    }

    public void loadNBTData(CompoundTag nbt) {
        if (nbt.contains("mjolnir_bind")) {
            ListTag listTag = nbt.getList("mjolnir_bind", 8);
            for (int i = 0; i < listTag.size(); i++) {
                String mjolnir = listTag.getString(i);
                this.mjolnirList.add(mjolnir);
            }
        }
    }
}
