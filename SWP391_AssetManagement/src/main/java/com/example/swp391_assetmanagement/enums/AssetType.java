package com.example.swp391_assetmanagement.enums;

public enum AssetType {

    LAPTOP(1, "LAPTOP"),
    MONITOR(2, "MONITOR"),
    PC(3, "PC"),
    DESK(4, "DESK"),
    CHAIR(5, "CHAIR"),
    PRINTER(6, "PRINTER");

    private final int value;
    private final String name;

    AssetType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
