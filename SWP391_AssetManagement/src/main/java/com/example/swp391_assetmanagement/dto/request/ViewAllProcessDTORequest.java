package com.example.swp391_assetmanagement.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ViewAllProcessDTORequest {

    private String requestStatusId;

    private String requestTypeId;

    private List<String> requestTypeIdList;

    private Integer pageIndex;
}
