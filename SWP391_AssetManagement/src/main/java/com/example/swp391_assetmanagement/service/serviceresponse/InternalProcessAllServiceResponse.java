package com.example.swp391_assetmanagement.service.serviceresponse;

import lombok.Getter;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;

import java.time.LocalDate;

@Entity
@Getter
public class InternalProcessAllServiceResponse {

    @Column(name = "asset_id")
    public Long assetId;

    @Column(name = "asset_request_id")
    public String assetRequestId;

    @Column(name = "asset_type_id")
    public String assetTypeId;

    @Column(name = "quantity")
    public Integer quantity;

    @Column(name = "from_location_id")
    public String fromLocationId;

    @Column(name = "to_location_id")
    public String toLocationId;

    @Column(name = "from_user_id")
    public String fromUserId;

    @Column(name = "to_user_id")
    public String toUserId;

//    @Column(name = "date_of_execution")
//    public LocalDate dateOfExecution;

//    @Column(name = "handover_date")
//    public LocalDate handoverDate;

    @Column(name = "note")
    public String note;

    @Column(name = "created_at")
    public LocalDate createdAt;

//    @Column(name = "approval_status_id")
//    public String approvalStatusId;

    @Column(name = "total_items")
    public Integer totalItems;
}
