package com.example.swp391_assetmanagement.enums;

public enum RequestType {

    ALLOCATION(1, "ALLOCATION"),
    RETRIEVAL(2, "RETRIEVAL"),
    PROCUREMENT(3, "PROCUREMENT"),
    MAINTENANCE(4, "MAINTENANCE"),
    LIQUIDATION(5, "LIQUIDATION");

    private final int value;
    private final String name;

    RequestType(int value, String name) {
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
