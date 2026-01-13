package com.example.swp391_assetmanagement.repository.entity;

import lombok.Setter;
import org.seasar.doma.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Entity
@Table(name = "assets")
public class Assets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "asset_code")
    public String assetCode;

    @Column(name = "asset_status_id")
    public Long assetStatusId;

    @Column(name = "asset_type_id")
    public Long assetTypeId;

    @Column(name = "received_date")
    public LocalDateTime receivedDate;

    @Column(name = "current_user_id")
    public Long currentUserId;

    @Column(name = "warranty_period")
    public LocalDate warrantyPeriod;

    @Column(name = "created_at")
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;
}
