package com.example.swp391_assetmanagement.entity;

import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "asset_external_request_detail")
public class AssetExternalRequestDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "asset_request_id")
    public Long assetRequestId;

    @Column(name = "asset_type_id")
    public String assetTypeId;

    @Column(name = "external_status_id")
    public String externalStatusId;

    @Column(name = "quantity")
    public Integer quantity;

    @Column(name = "note")
    public String note;

    @Column(name = "created_at")
    public LocalDateTime createdAt;
}
