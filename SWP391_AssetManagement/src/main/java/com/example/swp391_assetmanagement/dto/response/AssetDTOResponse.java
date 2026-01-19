package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssetDTOResponse {

    public String assetCode;

    public String description;
}
