package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ViewExternalProcessAllDTOResponse {

    private final List<ExternalProcessDTOResponse> externalProcessResponses;

    private final FilterExternalDTOResponse filters;

}
