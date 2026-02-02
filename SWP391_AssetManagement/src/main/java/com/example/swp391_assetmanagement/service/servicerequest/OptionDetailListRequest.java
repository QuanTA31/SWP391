package com.example.swp391_assetmanagement.service.servicerequest;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OptionDetailListRequest {

    private Long requestDetailId;

    private Boolean isSelected;

    private Integer offset;

    private Integer pageSize;
}
