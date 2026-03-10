package com.example.swp391_assetmanagement.entity;

import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "assets_asset_request_external")
public class AssetsAssetRequestExternal {

    @Id
    @Column(name = "assetId")
    private Long assetId;

    @Id
    @Column(name = "assetExternalRequestDetailId")
    private Long assetExternalRequestDetailId;

    @Column(name = "create_at")
    private LocalDateTime createdAt;
}