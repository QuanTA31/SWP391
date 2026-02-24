package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ViewAllUserDTOResponse {

    private final List<UserDTOResponse> userDTOResponses;

    private final FilterUserDTOResponse filters;
}
