package com.example.swp391_assetmanagement.service.servicerequest;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ViewAllUserServiceRequest {

    private String locationId;

    private String roleID;

    private String userStatus;

    private String name;

    private Integer offset;

    private Integer pageSize;
}
