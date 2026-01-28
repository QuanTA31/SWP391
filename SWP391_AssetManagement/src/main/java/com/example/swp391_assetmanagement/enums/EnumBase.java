package com.example.swp391_assetmanagement.enums;

import java.util.Arrays;

public interface EnumBase<E extends Enum<E>> {

    String getValue();

    static <E extends EnumBase<?>> E of(Class<E> enumClass, String value) {
        if (value == null) {
            return null;
        }
        return Arrays.stream(enumClass.getEnumConstants()).filter(it -> it.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s does not have such value => [%s]", enumClass.getSimpleName(), value)));
    }

    static <E extends EnumBase<?>> boolean hasValue(Class<E> enumClass, String value) {
        if (value == null) {
            return false;
        }
        return Arrays.stream(enumClass.getEnumConstants()).anyMatch(it -> it.getValue().equals(value));
    }
}
