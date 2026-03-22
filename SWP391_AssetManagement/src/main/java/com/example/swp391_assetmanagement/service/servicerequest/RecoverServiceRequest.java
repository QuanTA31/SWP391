package com.example.swp391_assetmanagement.service.servicerequest;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecoverServiceRequest {

        private Long detailId;

        private String targetStatus; // "08" - STOCKED

        private String targetLocation;
}
