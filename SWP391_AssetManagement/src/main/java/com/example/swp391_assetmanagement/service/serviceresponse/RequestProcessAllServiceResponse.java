package com.example.swp391_assetmanagement.service.serviceresponse;

import lombok.Getter;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;

import java.time.LocalDate;

@Entity
@Getter
public class RequestProcessAllServiceResponse {

    @Column(name = "request_id")
    public Long requestId;

    @Column(name = "request_type_id")
    public String requestTypeId;

    @Column(name = "requested_by")
    public String requestedBy;

    @Column(name = "requested_date")
    public LocalDate requestedDate;

    @Column(name = "request_status_id")
    public String requestStatusId;

    @Column(name = "approval_by")
    public String approvalBy;

    @Column(name = "approval_date")
    public LocalDate approvalDate;

    @Column(name = "handover_date")
    public LocalDate handoverDate;

    @Column(name = "note")
    public String note;

    @Column(name = "created_at")
    public LocalDate createdAt;

    @Column(name = "total_items")
    public Integer totalItems;

}
