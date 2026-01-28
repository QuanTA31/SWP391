package com.example.swp391_assetmanagement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

@Getter
@AllArgsConstructor
@Domain(valueType = String.class, factoryMethod = "of")
public enum Roles implements EnumBase<Roles> {

    ADMIN("01", "ADMIN"),
    MANAGER("02", "MANAGER"),
    WAREHOUSE("03", "WAREHOUSE"),
    PURCHASING("04", "PURCHASING"),
    DEPARTMENT_MANAGER("05", "DEPARTMENT_MANAGER"),
    CLIENT("06", "CLIENT");

    private final String value;
    private final String name;

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static Roles of(String key) {
        return EnumBase.of(Roles.class, key);
    }

    public static boolean hasValue(String key) {
        return EnumBase.hasValue(Roles.class, key);
    }
}
