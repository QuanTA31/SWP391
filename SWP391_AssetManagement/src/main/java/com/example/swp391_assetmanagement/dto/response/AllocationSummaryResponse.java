package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AllocationSummaryResponse {
    private int draftCount;
    private int pendingCount;
    private int approveCount;
    private int inProgressCount;
    private int completedCount;
    private int cancelledCount;
    private int totalCount;
}
