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

    private Integer page;

    private Integer pageSize;

    private Integer totalAsset;

    private Integer totalPages;

    private boolean hasNextPage;

    private boolean hasPreviousPage;

    @Builder
    @Getter
    public static class AssetDetailResponse {

        private String assetCode;

        private String description;

        private LocalDate receivedDate;

        private String locationName;

        private String assetStatusId;

        private String assetTypeId;

        private String username;

        private String name;

        private String userStatus;
    }
}
