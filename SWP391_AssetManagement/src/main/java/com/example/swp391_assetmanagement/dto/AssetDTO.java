package com.example.swp391_assetmanagement.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssetDTO {
    private String assetCode;

    private String description;
}
