package com.example.swp391_assetmanagement.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ViewExternalProcessRequest {

    private String requestStatusId;

    private String requestTypeId;

//    private String approvalStatusId;

    private Integer pageIndex;

}
