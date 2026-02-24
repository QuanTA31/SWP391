package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ViewAllProcessDTOResponse {

    private final List<AllProcessDTOResponse> allProcessResponses;

    private final FilterAllDTOResponse filters;

}
