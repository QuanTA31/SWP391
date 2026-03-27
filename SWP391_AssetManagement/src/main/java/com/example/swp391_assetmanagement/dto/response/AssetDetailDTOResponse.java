package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
@Builder
@Getter
public class AssetDetailDTOResponse {

    private String assetCode;

    private String description;

    private LocalDate receivedDate;

    private String locationName;

    private String assetStatusName;

    private String assetTypeName;

    private String username;

    private String userStatus;
}
