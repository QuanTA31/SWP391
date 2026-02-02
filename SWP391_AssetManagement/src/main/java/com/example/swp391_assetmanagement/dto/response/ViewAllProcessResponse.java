package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ViewAllProcessResponse {

    private final List<AllProcessResponse> allProcessResponses;

    private final FilterAllResponse filters;

}
