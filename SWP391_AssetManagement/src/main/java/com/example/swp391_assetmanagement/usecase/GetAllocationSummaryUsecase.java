package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dao.AllocationSummaryDAO;
import com.example.swp391_assetmanagement.dto.response.AllocationSummaryResponse;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GetAllocationSummaryUsecase {

    private final AllocationSummaryDAO allocationSummaryDAO;
    private final UserService userService;

    @Transactional(readOnly = true)
    public AllocationSummaryResponse execute(HttpSession session) {
        String userCode = (String) session.getAttribute("USER_CODE");
        if (userCode == null) return null;

        Long userId = userService.getIdByUserCode(userCode);
        if (userId == null) return null;

        List<Map<String, Object>> results = allocationSummaryDAO.countRequestStatusByUser(userId);

        int draft = 0, pending = 0, approved = 0, inProgress = 0, completed = 0, cancelled = 0, total = 0;

        for (Map<String, Object> map : results) {
            String statusId = (String) map.get("request_status_id");
            Number countNum = (Number) map.get("count");
            int count = countNum != null ? countNum.intValue() : 0;
            total += count;

            if (RequestStatus.DRAFT.getValue().equals(statusId)) draft = count;
            else if (RequestStatus.PENDING_APPROVAL.getValue().equals(statusId)) pending = count;
            else if (RequestStatus.APPROVED.getValue().equals(statusId)) approved = count;
            else if (RequestStatus.IN_PROGRESS.getValue().equals(statusId)) inProgress = count;
            else if (RequestStatus.COMPLETED.getValue().equals(statusId)) completed = count;
            else if (RequestStatus.CANCELLED.getValue().equals(statusId)) cancelled = count;
        }

        return AllocationSummaryResponse.builder()
                .draftCount(draft)
                .pendingCount(pending)
                .approveCount(approved)
                .inProgressCount(inProgress)
                .completedCount(completed)
                .cancelledCount(cancelled)
                .totalCount(total)
                .build();
    }
}
