package com.example.swp391_assetmanagement.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

@Getter
@AllArgsConstructor
@Domain(valueType = String.class, factoryMethod = "of")
public enum RequestStatus implements EnumBase<RequestStatus> {

    DRAFT("01", "DRAFT"),
    SUBMITTED("02", "SUBMITTED"),
    PENDING_APPROVAL("03", "PENDING_APPROVAL"),
    APPROVED("04", "APPROVED"),
    IN_PROGRESS("05", "IN_PROGRESS"),
    COMPLETED("06", "COMPLETED"),
    CANCELLED("07", "CANCELLED"),
    WAITING_PARENT("08", "WAITING_PARENT");

    private final String value;
    private final String name;

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static RequestStatus of(String key) {
        return EnumBase.of(RequestStatus.class, key);
    }

    public static boolean hasValue(String key) {
        return EnumBase.hasValue(RequestStatus.class, key);
    }
}
