package com.example.swp391_assetmanagement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

@Getter
@AllArgsConstructor
@Domain(valueType = String.class, factoryMethod = "of")
public enum ApprovalStatus implements EnumBase<ApprovalStatus> {

    PENDING("01", "PENDING"),
    APPROVED("02", "APPROVED"),
    REJECTED("03", "REJECTED");

    private final String value;        // id trong DB
    private final String name;       // name trong DB

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static ApprovalStatus of(String key) {
        return EnumBase.of(ApprovalStatus.class, key);
    }

    public static boolean hasValue(String key) {
        return EnumBase.hasValue(ApprovalStatus.class, key);
    }
}

