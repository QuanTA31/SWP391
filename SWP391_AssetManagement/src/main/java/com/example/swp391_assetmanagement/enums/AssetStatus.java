package com.example.swp391_assetmanagement.enums;

public enum AssetStatus {

    NEW(1, "NEW"),
    ASSIGNED(2, "ASSIGNED"),
    TRANSFERRING(3, "TRANSFERRING"),
    MAINTENANCE(4, "MAINTENANCE"),
    BROKEN(5, "BROKEN"),
    DISPOSED(6, "DISPOSED"),
    LOST(7, "LOST");

    private final int value;
    private final String name;

    AssetStatus(int value, String name) {
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
