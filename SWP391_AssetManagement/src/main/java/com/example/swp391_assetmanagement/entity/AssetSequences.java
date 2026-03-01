package com.example.swp391_assetmanagement.entity;

import lombok.Setter;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

@Setter
@Entity
@Table(name = "asset_sequences")
public class AssetSequences {

    @Id
    @Column(name = "asset_type")
    public String assetType;

    @Id
    @Column(name = "current_value")
    public Long currentValue;
}
