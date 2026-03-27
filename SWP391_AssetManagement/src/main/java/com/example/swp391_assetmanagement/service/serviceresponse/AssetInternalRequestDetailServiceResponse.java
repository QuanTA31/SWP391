package com.example.swp391_assetmanagement.service.serviceresponse;

import lombok.Getter;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;

@Getter
@Entity
public class AssetInternalRequestDetailServiceResponse {

    @Column(name = "asset_id")
    private Long assetId;

    @Column(name = "is_done")
    private Boolean isDone;

    @Column(name = "note")
    private String note;
}
