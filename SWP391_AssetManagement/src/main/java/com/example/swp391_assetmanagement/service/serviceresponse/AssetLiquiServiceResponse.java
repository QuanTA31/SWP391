package com.example.swp391_assetmanagement.service.serviceresponse;

import lombok.Getter;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;

@Entity
@Getter
public class AssetLiquiServiceResponse {

    @Column(name = "id")
    public Long assetId;

    @Column(name = "location_id")
    public String locationId;

    @Column(name = "asset_type_id")
    public String assetTypeId;
}
