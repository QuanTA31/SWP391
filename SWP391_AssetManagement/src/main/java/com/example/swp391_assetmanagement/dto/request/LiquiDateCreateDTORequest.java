package com.example.swp391_assetmanagement.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LiquiDateCreateDTORequest {

    private String locationId;

    private String assetTypeId;

    private String assetStatusId;

    private String searchWord;

    private Integer pageIndex;

    private Integer assetRequestId;

    //private String note;
}
