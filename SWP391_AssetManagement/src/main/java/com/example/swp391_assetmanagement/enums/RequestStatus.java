package com.example.swp391_assetmanagement.enums;

public enum RequestStatus {

    DRAFT(1, "DRAFT"),
    SUBMITTED(2, "SUBMITTED"),
    PENDING_APPROVAL(3, "PENDING_APPROVAL"),
    APPROVED(4, "APPROVED"),
    IN_PROGRESS(5, "IN_PROGRESS"),
    COMPLETED(6, "COMPLETED"),
    CANCELLED(7, "CANCELLED"),
    WAITING_PARENT(8, "WAITING_PARENT");

    private final int value;
    private final String name;

    RequestStatus(int value, String name) {
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
