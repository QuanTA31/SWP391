package com.example.swp391_assetmanagement.service.servicerequest;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OptionDetailSelectServiceRequest {

    private Long optionDetailId;

    private boolean isSelected;
}
