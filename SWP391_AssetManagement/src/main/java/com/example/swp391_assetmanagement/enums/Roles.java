package com.example.swp391_assetmanagement.enums;

public enum Roles {

    ADMIN(1, "ADMIN"),
    MANAGER(2, "MANAGER"),
    WAREHOUSE(3, "WAREHOUSE"),
    PURCHASING(4, "PURCHASING"),
    DEPARTMENT_MANAGER(5, "DEPARTMENT_MANAGER"),
    CLIENT(6, "CLIENT");

    private final int value;
    private final String name;

    Roles (int value, String name) {
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
