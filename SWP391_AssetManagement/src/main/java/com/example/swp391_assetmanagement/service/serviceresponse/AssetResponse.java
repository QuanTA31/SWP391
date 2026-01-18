package com.example.swp391_assetmanagement.service.serviceresponse;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssetResponse {

    private String assetCode;

    private String description;
}
