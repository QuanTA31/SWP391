package com.example.swp391_assetmanagement.repository.entity;

import lombok.Setter;
import org.seasar.doma.*;

import java.time.LocalDateTime;

@Setter
@Entity
@Table(name = "procurement_request_management")
public class ProcurementRequestManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "request_code")
    public String requestCode;

    @Column(name = "asset_type_id")
    public Long assetTypeId;

    @Column(name = "request_type_id")
    public Long requestTypeId;

    @Column(name = "request_status_id")
    public Long requestStatusId;

    public Long quantity;

    @Column(name = "expected_unit_price")
    public Double expectedUnitPrice;

    @Column(name = "actual_unit_price")
    public Double actualUnitPrice;

    @Column(name = "created_at")
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    public String reason;
}
