package com.example.swp391_assetmanagement.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ViewAllUserDTORequest {

    private String locationId;

    private String roleID;

    private String name;

    private String status;

    private Integer pageIndex;
}
