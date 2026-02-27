package com.example.swp391_assetmanagement.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OptionDetailListDTORequest {

    private Long requestDetailId;

    private Boolean isSelected;

    private Integer offset;

    private Integer pageSize;
}
