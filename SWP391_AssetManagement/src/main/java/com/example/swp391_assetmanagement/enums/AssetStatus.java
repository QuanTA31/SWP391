package com.example.swp391_assetmanagement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

@Getter
@AllArgsConstructor
@Domain(valueType = String.class, factoryMethod = "of")
public enum AssetStatus implements EnumBase<AssetStatus> {

    STOCK_IN("00", "STOCK_IN"),
    NEW("01", "NEW"),
    ASSIGNED("02", "ASSIGNED"),
    TRANSFERRING("03", "TRANSFERRING"),
    MAINTENANCE("04", "MAINTENANCE"),
    BROKEN("05", "BROKEN"),
    DISPOSED("06", "DISPOSED"),
    LOST("07", "LOST"),
    STOCKED("08", "STOCKED"),
    RETRIVAL("09","RETRIVAL"),

    LIQUIDATION("10", "LIQUIDATION"),

    IN_PROGRESS("11", "IN_PROGRESS");

    private final String value;
    private final String name;

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static AssetStatus of(String key) {
        return EnumBase.of(AssetStatus.class, key);
    }

    public static boolean hasValue(String key) {
        return EnumBase.hasValue(AssetStatus.class, key);
    }
}
