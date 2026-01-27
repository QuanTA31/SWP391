package com.example.swp391_assetmanagement.enums;

public enum Location {

    HEAD_OFFICE(1, "HEAD_OFFICE"),
    BRANCH_OFFICE(2, "BRANCH_OFFICE"),
    MEETING_ROOM(3, "MEETING_ROOM"),
    IT_ROOM(4, "IT_ROOM"),
    WAREHOUSE(5, "WAREHOUSE"),
    OUTSIDE_COMPANY(6, "OUTSIDE_COMPANY");

    private final int value;
    private final String name;

    Location(int value, String name) {
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
