package com.deriys.divinerelics.capabilities.thor;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;

import java.util.ArrayList;
import java.util.List;

public class FightsThor {
    private List<String> thorList = new ArrayList<>();
    private boolean hasLoggedOut = false;

    public boolean hasLoggedOut() {
        return hasLoggedOut;
    }

    public void setHasLoggedOut(boolean hasLoggedOut) {
        this.hasLoggedOut = hasLoggedOut;
    }

    public List<String> getThorList() {
        return thorList;
    }

    public void addThor(String thor) {
        this.thorList.add(thor);
    }

    public void removeThor(String thor) {
        this.thorList.remove(thor);
    }

    public void copyFrom(FightsThor source) {
        this.thorList = source.thorList;
        this.hasLoggedOut = source.hasLoggedOut();
    }

    public void saveNBTData(CompoundTag nbt) {
        if (!thorList.isEmpty()) {
            ListTag listTag = nbt.getList("fights_thor", 8);
            for (String mjolnir: this.thorList) {
                listTag.add(StringTag.valueOf(mjolnir));
            }
            nbt.put("fights_thor", listTag);
        }
        nbt.putBoolean("has_logged_out", hasLoggedOut);
    }

    public void loadNBTData(CompoundTag nbt) {
        if (nbt.contains("fights_thor")) {
            ListTag listTag = nbt.getList("fights_thor", 8);
            for (int i = 0; i < listTag.size(); i++) {
                String mjolnir = listTag.getString(i);
                this.thorList.add(mjolnir);
            }
        }
        this.hasLoggedOut = nbt.getBoolean("has_logged_out");
    }
}
