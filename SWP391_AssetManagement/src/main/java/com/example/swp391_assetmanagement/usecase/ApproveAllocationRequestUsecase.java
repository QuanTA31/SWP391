package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.service.AllocationService;
import com.example.swp391_assetmanagement.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ApproveAllocationRequestUsecase {

    private final AllocationService allocationService;
    //private final UserDAO userDAO;
    private final UserService userService;

    @Transactional
    public void approve(Long requestId, HttpSession session) {
        updateStatus(requestId, RequestStatus.APPROVED, session);
    }

    @Transactional
    public void reject(Long requestId, HttpSession session) {
        updateStatus(requestId, RequestStatus.CANCELLED, session);
    }

//            2.  Lấy ID của Quản lý từ Session (`USER_CODE`).
//            4.  Gán `approvedBy` là ID của Quản lý và `approvedDate` là ngày hiện tại.

    private void updateStatus(Long requestId, RequestStatus newStatus, HttpSession session) {

        //1.  Lấy thông tin `AssetRequest` từ DB.
        Optional<AssetRequest> assetRequest = allocationService.getAssetRequestById(requestId);
        if (assetRequest.isEmpty()) {
            throw new IllegalArgumentException("Allocation request not found.");
        }

        String userCode = (String) session.getAttribute("USER_CODE");
//        Long managerId = userDAO.findIdByUserCode(userCode);
        Long managerId = userService.getIdByUserCode(userCode);

        //3.  Cập nhật trạng thái mới (`APPROVED` - 03 hoặc `CANCELLED` - 07).
        assetRequest.get().requestStatusId = newStatus.getValue();
        assetRequest.get().approvedBy = managerId;
        assetRequest.get().approvedDate = LocalDate.now();

        //5. lưu thay đổi.
        allocationService.updateAssetRequest(assetRequest.orElse(null));
    }
}
