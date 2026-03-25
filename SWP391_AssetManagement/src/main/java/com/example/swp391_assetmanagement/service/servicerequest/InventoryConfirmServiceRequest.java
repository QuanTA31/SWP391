package com.example.swp391_assetmanagement.service.servicerequest;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InventoryConfirmServiceRequest {
    private Long detailId;
    private String selectedStatus;
}
