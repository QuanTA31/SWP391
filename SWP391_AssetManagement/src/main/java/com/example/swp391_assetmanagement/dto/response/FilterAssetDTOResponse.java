package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FilterAssetDTOResponse {

    private String assetCode;

    private String locationId;

    private String assetTypeId;
}
