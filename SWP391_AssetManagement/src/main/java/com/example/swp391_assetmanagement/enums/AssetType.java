package com.example.swp391_assetmanagement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

@Getter
@AllArgsConstructor
@Domain(valueType = String.class, factoryMethod = "of")
public enum AssetType implements EnumBase<AssetType> {

    LAPTOP("01", "LAPTOP"),
    MONITOR("02", "MONITOR"),
    PC("03", "PC"),
    DESK("04", "DESK"),
    CHAIR("05", "CHAIR"),
    PRINTER("06", "PRINTER");

    private final String value;
    private final String name;

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static AssetType of(String key) {
        return EnumBase.of(AssetType.class, key);
    }

    public static boolean hasValue(String key) {
        return EnumBase.hasValue(AssetType.class, key);
    }
}
