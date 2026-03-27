package com.example.swp391_assetmanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewLiquidationAssetDTORequest {
    private Long assetRequestId;
    private String assetTypeId;
    private String searchWord;
    private Integer pageIndex;
    private Integer pageSize;
    private Integer totalItems;
    private Integer totalPages;
    private boolean hasPreviousPage;
    private boolean hasNextPage;
}
