package com.example.swp391_assetmanagement.service.servicerequest;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InternalProcessRequest {

    private Long requestId;

    private String requestStatusId;

    private String requestTypeId;

//    private String approvalStatusId;

    private Integer offset;

    private Integer pageSize;
}
