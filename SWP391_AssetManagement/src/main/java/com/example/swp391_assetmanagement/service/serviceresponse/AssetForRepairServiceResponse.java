package com.example.swp391_assetmanagement.service.serviceresponse;

import lombok.Getter;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;

@Getter
@Entity
public class AssetForRepairServiceResponse {

    @Column(name = "id")
    public Long id;

    @Column(name = "asset_code")
    public String assetCode;

    @Column(name = "asset_type_id")
    public String assetTypeId;

    @Column(name = "description")
    public String description;

    @Column(name = "asset_status_id")
    public String assetStatusId;
}
