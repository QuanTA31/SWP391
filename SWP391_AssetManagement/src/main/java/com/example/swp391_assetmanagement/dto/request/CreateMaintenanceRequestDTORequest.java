package com.example.swp391_assetmanagement.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMaintenanceRequestDTORequest {

    private Long assetId;

    private String issueDescription;

    private String priority;

    private String note;

    private Boolean isSubmitted;
}
