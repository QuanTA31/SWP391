package com.example.swp391_assetmanagement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

@Getter
@AllArgsConstructor
@Domain(valueType = String.class, factoryMethod = "of")
public enum ExternalStatus implements EnumBase<RequestType> {

    DRAFT("01", "DRAFT"),
    IN_PROGRESS("02", "IN_PROGRESS"),
    DONE("03", "DONE"),
    REJECT("04", "REJECT"),
    COMPLETED("05", "COMPLETED"),
    CANCEL("06", "CANCEL");


    private final String value;
    private final String name;

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static ExternalStatus of(String key) {
        return EnumBase.of(ExternalStatus.class, key);
    }

    public static boolean hasValue(String key) {
        return EnumBase.hasValue(ExternalStatus.class, key);
    }
}
