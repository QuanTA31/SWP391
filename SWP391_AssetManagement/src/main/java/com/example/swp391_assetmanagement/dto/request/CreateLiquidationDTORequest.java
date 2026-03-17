package com.example.swp391_assetmanagement.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class CreateLiquidationDTORequest {

    @NotEmpty
    private List<CreateLiquidationDetailDTORequest>  createLiquidationDetailDTORequestList;

    @NotNull
    private Boolean isSubmitted;

    private Long assetRequestId;

    private String requestStatus;
    //thêm
    private String requestTypeId;

    private LocalDate requestedDate;

}
