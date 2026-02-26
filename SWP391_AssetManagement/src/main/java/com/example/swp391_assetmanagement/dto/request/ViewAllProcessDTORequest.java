package com.example.swp391_assetmanagement.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ViewAllProcessDTORequest {

    private String requestStatusId;

    private String requestTypeId; //user chọn filter

    //   private String approvalStatusId;
    private List<String> requestTypeIdList;  //backend phân quyền

    private Integer pageIndex;
}
