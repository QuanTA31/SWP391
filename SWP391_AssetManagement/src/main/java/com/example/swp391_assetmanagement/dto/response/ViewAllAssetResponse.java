package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ViewAllAssetResponse {

    private final List<AssetResponse> assetResponses;

    private final FiltersResponse filters;
}
