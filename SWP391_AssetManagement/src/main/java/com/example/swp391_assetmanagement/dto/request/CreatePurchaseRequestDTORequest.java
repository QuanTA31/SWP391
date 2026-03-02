package com.example.swp391_assetmanagement.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePurchaseRequestDTORequest {

    @NotEmpty
    private List<CreatePurchaseRequestDetailDTORequest>  createPurchaseRequestDetailDTORequestList;

    @NotNull
    private Boolean isSubmitted;

    private Long assetRequestId;

    private String requestStatus;

}
