package com.example.swp391_assetmanagement.dao.daoresponse;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;

@Entity
public class AssetsDaoResponse {

    @Column(name = "asset_code")
    public String assetCode;

    @Column(name = "description")
    public String description;
}
