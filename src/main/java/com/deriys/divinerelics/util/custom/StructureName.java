package com.deriys.divinerelics.util.custom;

import java.util.Objects;

/**
 * The StructureName class represents a structure with a specified path and name.
 * This class is commonly used in scenarios where structures need to be uniquely identified
 * and managed using their path and name attributes.
 */
public class StructureName {
    private String path;
    private String name;

    public StructureName(String path, String name) {
        this.path = path;
        this.name = name;
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StructureName that = (StructureName) o;
        return Objects.equals(getPath(), that.getPath()) && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPath(), getName());
    }
}
