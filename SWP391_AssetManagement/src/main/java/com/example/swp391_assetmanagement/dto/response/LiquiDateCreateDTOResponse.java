package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LiquiDateCreateDTOResponse {

    private final List<LiquidateAssetDTOResponse> assetResponses;

    private final FiltersDTOResponse filters;
}
