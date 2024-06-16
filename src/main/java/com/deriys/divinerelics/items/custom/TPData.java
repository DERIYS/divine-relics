package com.deriys.divinerelics.items.custom;

public class TPData {
    private final int offset;
    private final boolean status;

    public TPData(int offset, boolean status) {
        this.offset = offset;
        this.status = status;
    }

    public int getOffset() {
        return offset;
    }

    public boolean getStatus() {
        return status;
    }
}
