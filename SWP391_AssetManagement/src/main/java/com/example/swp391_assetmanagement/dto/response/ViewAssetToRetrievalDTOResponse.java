package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ViewAssetToRetrievalDTOResponse {

    private List<AssetDetailDTOResponse> assets;

    private FilterAssetDTOResponse filters;

    private Integer page;

    private Integer pageSize;

    private Integer totalAsset;

    private Integer totalPages;

    private boolean hasNextPage;

    private boolean hasPreviousPage;
}