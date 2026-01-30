package com.example.swp391_assetmanagement.entity;

import lombok.Setter;
import org.seasar.doma.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Entity
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
    public Date requestedDate;

    @Column(name = "request_status_id")
    public String requestStatusId;

    @Column(name = "approved_by")
    public Long approvedBy;

    @Column(name = "approved_date")
    public Date approvedDate;

    @Column(name = "handover_date")
    public LocalDate handoverDate;

    @Column(name = "note")
    public String note;

    @Column(name = "created_at")
    public LocalDateTime createdAt;
}
