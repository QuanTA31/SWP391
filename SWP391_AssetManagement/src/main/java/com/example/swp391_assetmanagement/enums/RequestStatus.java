package com.example.swp391_assetmanagement.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

@Getter
@AllArgsConstructor
@Domain(valueType = String.class, factoryMethod = "of")
public enum RequestStatus implements EnumBase<RequestStatus> {

    DRAFT("01", "DRAFT"),
    PENDING_APPROVAL("02", "PENDING_APPROVAL"),
    APPROVED("03", "APPROVED"),
    RESEARCH("04", "RESEARCH"),
    IN_PROGRESS("05", "IN_PROGRESS"),
    COMPLETED("06", "COMPLETED"),
    CANCELLED("07", "CANCELLED"),
    RESEARCH_DONE("08", "RESEARCH_DONE");


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
