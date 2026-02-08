package com.example.swp391_assetmanagement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

@Getter
@AllArgsConstructor
@Domain(valueType = String.class, factoryMethod = "of")
public enum UserStatus implements EnumBase<UserStatus> {

    ACTIVE("01", "ACTIVE"),
    SUSPENDED("02", "SUSPENDED"),
    DISABLED("03", "DISABLED");

    private final String value;
    private final String name;

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static UserStatus of(String key) {
        return EnumBase.of(UserStatus.class, key);
    }

    public static boolean hasValue(String key) {
        return EnumBase.hasValue(UserStatus.class, key);
    }
}
