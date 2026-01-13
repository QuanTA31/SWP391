package com.example.swp391_assetmanagement.repository.entity;

import lombok.Setter;
import org.seasar.doma.*;

import java.time.LocalDateTime;

@Setter
@Entity
@Table(name = "asset_request_management")
public class AssetRequestManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "request_code")
    public String requestCode;

    @Column(name = "asset_id")
    public Long assetId;

    @Column(name = "request_status_id")
    public Long requestStatusId;

    @Column(name = "request_type_id")
    public Long requestTypeId;

    @Column(name = "from_user_id")
    public Long fromUserId;

    @Column(name = "to_user_id")
    public Long toUserId;

    @Column(name = "expected_unit_price")
    public Double expectedUnitPrice;

    @Column(name = "actual_unit_price")
    public Double actualUnitPrice;

    public String note;

    @Column(name = "created_at")
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;
}

