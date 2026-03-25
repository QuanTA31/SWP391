package com.example.swp391_assetmanagement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

@Getter
@AllArgsConstructor
@Domain(valueType = String.class, factoryMethod = "of")
public enum RequestType implements EnumBase<RequestType> {

    ALLOCATION("01", "ALLOCATION"),
    RETRIEVAL("02", "RETRIEVAL"),
    PROCUREMENT("03", "PROCUREMENT"),
    MAINTENANCE("04", "MAINTENANCE"),
    LIQUIDATION("05", "LIQUIDATION"),
    INVENTORY("06", "INVENTORY");

    private final String value;
    private final String name;

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static RequestType of(String key) {
        return EnumBase.of(RequestType.class, key);
    }

    public static boolean hasValue(String key) {
        return EnumBase.hasValue(RequestType.class, key);
    }
}
