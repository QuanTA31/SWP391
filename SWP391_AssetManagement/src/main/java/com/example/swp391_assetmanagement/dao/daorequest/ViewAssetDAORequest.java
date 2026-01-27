package com.example.swp391_assetmanagement.dao.daorequest;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ViewAssetDAORequest {

    private String locationId;

    private String assetTypeId;

    private String assetStatusId;

    private Integer offset;

    private Integer pageSize;
}
