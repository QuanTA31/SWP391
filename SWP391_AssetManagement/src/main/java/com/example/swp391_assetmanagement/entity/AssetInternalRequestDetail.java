package com.example.swp391_assetmanagement.entity;

import lombok.Setter;
import org.seasar.doma.*;

import java.time.LocalDateTime;

@Setter
@Entity
@Table(name = "asset_internal_request_detail")
public class AssetInternalRequestDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "asset_id")
    public Long assetId;

    @Column(name = "asset_request_id")
    public Long assetRequestId;

    @Column(name = "asset_type_id")
    public String assetTypeId;

    @Column(name = "quantity")
    public Integer quantity;

    @Column(name = "from_location_id")
    public String fromLocationId;

    @Column(name = "to_location_id")
    public String toLocationId;

    @Column(name = "from_user_id")
    public Long fromUserId;

    @Column(name = "to_user_id")
    public Long toUserId;

    @Column(name = "note")
    public String note;

    @Column(name = "created_at")
    public LocalDateTime createdAt;
}
