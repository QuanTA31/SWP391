package com.example.swp391_assetmanagement.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OptionDetailSelectDTORequest {

    private Long optionDetailId;

    private boolean isSelected;
}
