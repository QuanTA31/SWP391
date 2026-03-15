package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.seasar.doma.Column;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
public class ViewAssetByUserDisabledDTOResponse {
    private List<AssetDetailResponse> assets;
    private FilterUserDTOResponse filters; // THÊM DÒNG NÀY

    private Integer page;
    private Integer pageSize;
    private Integer totalAsset;
    private Integer totalPages;
    private boolean hasNextPage;
    private boolean hasPreviousPage;

    // Tận dụng lại class Filter bạn đã có hoặc tạo mới
    @Builder
    @Getter
    public static class FilterUserDTOResponse {
        private String assetCode;
        private String locationId;
        private String assetTypeId;
    }

    @Builder
    @Getter
    public static class AssetDetailResponse {
        // ... (giữ nguyên các field cũ)
        private String assetCode;
        private String description;
        private LocalDate receivedDate;
        private String locationName;
        private String assetStatusName;
        private String assetTypeName;
        private String username;
        private String userStatus;
    }
}