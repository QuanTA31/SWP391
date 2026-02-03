package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FilterExternalResponse {

    private String requestStatusId;

    private String requestTypeId;

//    private String approvalStatusId;

    private final Integer page;

    private final Integer pageSize;

    private final Integer totalItems;

    private final Integer totalPages;

    private boolean hasNextPage;

    private boolean hasPreviousPage;

}
