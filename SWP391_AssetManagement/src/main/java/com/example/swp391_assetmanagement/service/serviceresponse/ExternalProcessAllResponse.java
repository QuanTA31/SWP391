package com.example.swp391_assetmanagement.service.serviceresponse;

import lombok.Getter;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;

import java.time.LocalDate;

@Entity
@Getter
public class ExternalProcessAllResponse {

    @Column(name = "asset_id")
    public String assetId;

    @Column(name = "asset_type_id")
    public String assetTypeId;

    @Column(name = "request_status_id")
    public String requestStatusId;

    @Column(name = "request_type_id")
    public String requestTypeId;

    @Column(name = "quantity")
    public Long quantity;

    @Column(name = "handover_date")
    public LocalDate handoverDate;

    @Column(name = "note")
    public String note;

    @Column(name = "approval_status_id")
    public String approvalStatusId;

    @Column(name = "option_detail")
    public Long optionDetail;

    @Column(name = "total_items")
    public Integer totalItems;

}
