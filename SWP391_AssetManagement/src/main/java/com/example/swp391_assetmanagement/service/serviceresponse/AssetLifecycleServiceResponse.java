package com.example.swp391_assetmanagement.service.serviceresponse;

import lombok.Getter;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;

import java.time.LocalDate;

@Entity
@Getter
public class AssetLifecycleServiceResponse {

    @Column(name = "request_id")
    public Long requestId;

    @Column(name = "request_type_id")
    public String requestTypeId;

    @Column(name = "request_status_id")
    public String requestStatusId;

    @Column(name = "requested_date")
    public LocalDate requestedDate;

    @Column(name = "requested_by")
    public Long requestedBy;

    @Column(name = "approved_date")
    public LocalDate approvedDate;

    @Column(name = "approved_by")
    public Long approvedBy;

    @Column(name = "handover_date")
    public LocalDate handoverDate;

    @Column(name = "note")
    public String note;

    @Column(name = "total_items")
    public Integer totalItems;
}
