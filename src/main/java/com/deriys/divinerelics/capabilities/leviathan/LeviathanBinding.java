package com.deriys.divinerelics.capabilities.leviathan;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;

import java.util.ArrayList;
import java.util.List;

public class LeviathanBinding {
    /**
     * Upper bound for remembered thrown weapons. Entries are only removed when the weapon is picked up again, so the
     * UUIDs of weapons that vanished otherwise (void, /kill, despawn) used to pile up in the player data forever.
     */
    private static final int MAX_ENTRIES = 16;

    private List<String> leviathanList = new ArrayList<>();

    public List<String> getLeviathanList() {
        return leviathanList;
    }

    public void addLeviathan(String leviathan) {
        if (this.leviathanList.contains(leviathan)) {
            return;
        }
        if (this.leviathanList.size() >= MAX_ENTRIES) {
            this.leviathanList.remove(0);
        }
        this.leviathanList.add(leviathan);
    }

    public void removeLeviathan(String leviathan) {
        this.leviathanList.remove(leviathan);
    }

    public void copyFrom(LeviathanBinding source) {
        this.leviathanList = new ArrayList<>(source.leviathanList);
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
        this.leviathanList.clear(); // loading into an already populated capability must not duplicate entries
        if (nbt.contains("leviathan_bind")) {
            ListTag listTag = nbt.getList("leviathan_bind", 8);
            for (int i = 0; i < listTag.size(); i++) {
                String mjolnir = listTag.getString(i);
                this.addLeviathan(mjolnir);
            }
        }
    }
}
