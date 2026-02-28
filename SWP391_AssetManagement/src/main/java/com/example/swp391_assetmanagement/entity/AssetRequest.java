package com.example.swp391_assetmanagement.entity;

import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Entity
@Getter

@Table(name = "asset_request")
public class AssetRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "request_type_id")
    public String requestTypeId;

    @Column(name = "requested_by")
    public Long requestedBy;

    @Column(name = "requested_date")
    public LocalDate requestedDate;

    @Column(name = "request_status_id")
    public String requestStatusId;

    @Column(name = "approved_by")
    public Long approvedBy;

    @Column(name = "approved_date")
    public LocalDate approvedDate;

    @Column(name = "handover_date")
    public LocalDate handoverDate;

    @Column(name = "note")
    public String note;

    @Column(name = "created_at")
    public LocalDateTime createdAt;
}
