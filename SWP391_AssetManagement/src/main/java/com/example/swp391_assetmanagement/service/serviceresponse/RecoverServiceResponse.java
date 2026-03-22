package com.example.swp391_assetmanagement.service.serviceresponse;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecoverServiceResponse {

    private boolean success;

    private Long detailId;
}
