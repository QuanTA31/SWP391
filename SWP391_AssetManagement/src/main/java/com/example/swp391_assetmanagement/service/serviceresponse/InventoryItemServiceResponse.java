package com.example.swp391_assetmanagement.service.serviceresponse;

import lombok.Builder;
import lombok.Getter;
import org.seasar.doma.Entity;

@Getter
@Entity
public class InventoryItemServiceResponse {

    private Long detailId;

    private String assetCode;

    private String assetTypeId;

    private String userFullName;

    private Boolean isDone;

    private Long assetId;

    private String statusId;
}
