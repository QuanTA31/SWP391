package com.example.swp391_assetmanagement.enums;

public enum ApprovalStatus {

    PENDING(1, "PENDING"),
    APPROVED(2, "APPROVED"),
    REJECTED(3, "REJECTED");

    private final int value;        // id trong DB
    private final String name;       // name trong DB

    ApprovalStatus(int value, String name) {
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

