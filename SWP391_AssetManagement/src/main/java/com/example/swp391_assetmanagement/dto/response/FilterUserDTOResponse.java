package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FilterUserDTOResponse {

    private final String roleId;

    private final String locationId;

    private final String status;

    private final String name;

    private final Integer page;

    private final Integer pageSize;

    private final Integer totalUser;

    private final Integer totalPages;

    private final boolean hasNextPage;

    private final boolean hasPreviousPage;

}
