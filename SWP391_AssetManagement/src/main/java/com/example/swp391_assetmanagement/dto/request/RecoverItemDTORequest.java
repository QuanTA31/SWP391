package com.example.swp391_assetmanagement.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecoverItemDTORequest {

    private Long detailId;

    public String targetAssetStatus;

    public String targetLocationId;
}
