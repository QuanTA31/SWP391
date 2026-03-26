package com.example.swp391_assetmanagement.service.serviceresponse;

import lombok.Builder;
import lombok.Getter;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;

@Getter
@Entity
public class InventoryItemServiceResponse {

    @Column(name = "id")
    private Long detailId;

    @Column(name = "asset_code")
    private String assetCode;

    @Column(name = "asset_type_id")
    private String assetTypeId;

    @Column(name = "name")
    private String userFullName;

    @Column(name = "is_done")
    private Boolean isDone;

    @Column(name = "id")
    private Long assetId;

    @Column(name = "status_id")
    private String statusId;
}
