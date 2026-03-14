package com.example.swp391_assetmanagement.service.serviceresponse;

import lombok.Getter;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;

@Getter
@Entity
public class OptionDetailSelectServiceResponse {

    @Column(name = "id")
    private Long optionDetailId;

    @Column(name = "is_selected")
    private boolean isSelected;
}
