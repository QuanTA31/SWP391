package com.example.swp391_assetmanagement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

@Getter
@AllArgsConstructor
@Domain(valueType = String.class, factoryMethod = "of")
public enum ExternalStatus implements EnumBase<RequestType> {

    IN_PROGRESS("01", "IN_PROGRESS"),
    UN_REPAIR("02", "UN_REPAIR"),
    STOCK_IN("03", "STOCK_IN"),
    COMPLETED("04", "COMPLETED");

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
