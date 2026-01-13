package com.example.swp391_assetmanagement.repository.entity;

import lombok.Setter;
import org.seasar.doma.*;

@Setter
@Entity
@Table(name = "asset_type")
public class AssetType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "name")
    public String name;
}
