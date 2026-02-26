package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ViewInternalProcessAllDTOResponse {

    private final List<InternalProcessDTOResponse> internalProcessResponses;

    private final FilterInternalDTOResponse filters;

}
