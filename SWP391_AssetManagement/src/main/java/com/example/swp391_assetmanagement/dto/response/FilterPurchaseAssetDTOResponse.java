package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FilterPurchaseAssetDTOResponse {

    private final Long assetRequestId;

    private final String assetTypeId;

    private final String searchWord;

    private final Integer page;

    private final Integer pageSize;

    private final Integer totalItems;

    private final Integer totalPages;

    private boolean hasNextPage;

    private boolean hasPreviousPage;
}
