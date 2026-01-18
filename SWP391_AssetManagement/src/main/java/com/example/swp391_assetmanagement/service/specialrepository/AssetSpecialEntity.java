package com.example.swp391_assetmanagement.service.specialrepository;

import org.seasar.doma.*;

@Entity
public class AssetSpecialEntity {

    @Column(name = "asset_code")
    public String assetCode;

    @Column(name = "description")
    public String description;
}
