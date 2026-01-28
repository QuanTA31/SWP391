package com.example.swp391_assetmanagement.service.serviceresponse;

import lombok.Getter;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;

import java.time.LocalDate;

@Entity
@Getter
public class InternalProcessAllResponse {

    @Column(name = "asset_id")
    public String assetId;

    @Column(name = "request_status_id")
    public String requestStatusId;

    @Column(name = "request_type_id")
    public String requestTypeId;

    @Column(name = "from_user_id")
    public Long fromUserId;

    @Column(name = "to_user_id")
    public Long toUserId;

    @Column(name = "date_of_execution")
    public LocalDate dateOfExecution;

    @Column(name = "handover_date")
    public LocalDate handoverDate;

    @Column(name = "note")
    public String note;

    @Column(name = "approval_status_id")
    public String approvalStatusId;

    @Column(name = "total_items")
    public Integer totalItems;
}
