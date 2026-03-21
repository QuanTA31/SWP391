package com.example.swp391_assetmanagement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

@Getter
@AllArgsConstructor
@Domain(valueType = String.class, factoryMethod = "of")
public enum Location implements EnumBase<Location> {

    HEAD_OFFICE("01", "HEAD_OFFICE"),
    BRANCH_OFFICE("02", "BRANCH_OFFICE"),
    MEETING_ROOM("03", "MEETING_ROOM"),
    IT_ROOM("04", "IT_ROOM"),

    WAREHOUSE("05", "WAREHOUSE"),
    OUTSIDE_COMPANY("06", "OUTSIDE_COMPANY");

    private final String value;
    private final String name;

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static Location of(String key) {
        return EnumBase.of(Location.class, key);
    }

    public static boolean hasValue(String key) {
        return EnumBase.hasValue(Location.class, key);
    }
}
