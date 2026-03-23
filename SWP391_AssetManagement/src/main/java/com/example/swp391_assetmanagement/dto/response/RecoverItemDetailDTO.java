package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RecoverItemDetailDTO {
    private Long detailId;

    private Long assetId;

    private String assetCode;

    private String assetTypeName;

    private String fromUserName;

    private String fromLocationName;

    private Boolean isDone;
}
