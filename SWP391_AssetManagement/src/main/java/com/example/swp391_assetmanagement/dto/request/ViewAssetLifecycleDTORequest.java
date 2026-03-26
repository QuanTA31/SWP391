package com.example.swp391_assetmanagement.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ViewAssetLifecycleDTORequest {

    private String assetCode;

    private String requestTypeId;

    private Integer pageIndex;
}
