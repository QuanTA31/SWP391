package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ViewAllAssetDTOResponse {

    private final List<AssetDTOResponse> assetResponses;

    private final FiltersDTOResponse filters;
}
