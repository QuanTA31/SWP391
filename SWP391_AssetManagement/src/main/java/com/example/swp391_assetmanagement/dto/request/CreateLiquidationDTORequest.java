package com.example.swp391_assetmanagement.dto.request;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateLiquidationDTORequest {

    private List<CreateLiquidationDetailDTORequest>  createLiquidationDetailDTORequestList;

    private Boolean isSubmitted;

    private Long assetRequestId;

    private String requestStatus;

    private String requestTypeId;

    private LocalDate requestedDate;

}
