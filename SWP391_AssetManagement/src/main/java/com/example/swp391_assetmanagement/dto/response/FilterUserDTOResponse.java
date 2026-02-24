package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FilterUserDTOResponse {

    private final String roleId;      // Thay cho assetTypeId

    private final String locationId;

    private final String name;  // Tìm theo Name

    private final Integer page;

    private final Integer pageSize;

    private final Integer totalUser;

    private final Integer totalPages;

    private final boolean hasNextPage;

    private final boolean hasPreviousPage;

}
