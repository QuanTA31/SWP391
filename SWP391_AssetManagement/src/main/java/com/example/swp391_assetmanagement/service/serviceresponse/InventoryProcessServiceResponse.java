package com.example.swp391_assetmanagement.service.serviceresponse;

import com.example.swp391_assetmanagement.entity.AssetRequest;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class InventoryProcessServiceResponse {
    private AssetRequest header;
    private List<InventoryItemServiceResponse> items;
}
