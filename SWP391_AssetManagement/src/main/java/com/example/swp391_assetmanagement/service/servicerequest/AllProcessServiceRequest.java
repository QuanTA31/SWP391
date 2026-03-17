package com.example.swp391_assetmanagement.service.servicerequest;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AllProcessServiceRequest {

    private String requestStatusId;

    private String requestTypeId;

 //   private String approvalStatusId;

    private List<String> requestTypeIdList;

    private List<String> excludeStatusIdList;

    private Integer offset;

    private Integer pageSize;
}
