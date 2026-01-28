package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ViewInternalProcessAllResponse {

    private final List<InternalProcessResponse> internalProcessResponses;

    private final FilterInternalResponse filters;

}
