package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dao.AssetRequestDAO;
import com.example.swp391_assetmanagement.dto.request.CreateMaintenanceRequestDTORequest;
import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.service.AssetInternalRequestDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class GetMaintenanceRequestDetailUsecase {

    private final AssetRequestDAO assetRequestDAO;
    private final AssetInternalRequestDetailService assetInternalRequestDetailService;
    private final com.example.swp391_assetmanagement.service.UserService userService;

    public MaintenanceRequestDetailResult execute(Long assetRequestId, String userCode) {
        
        AssetRequest assetRequest = assetRequestDAO.findAssetRequestByIdForUpdate(assetRequestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy yêu cầu"));

        AssetInternalRequestDetail detail = assetInternalRequestDetailService.findByAssetRequestId(assetRequestId);
        if (detail == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dữ liệu chi tiết bị lỗi");
        }

        // Check if current user is owner
        boolean isOwner = false;
        if (userCode != null) {
            Long userId = userService.getIdByUserCode(userCode);
            isOwner = assetRequest.requestedBy.equals(userId);
        }

        // Parse note field back to DTO
        // Note format: "[PRIORITY] Issue Description | Note: Optional Notes"
        CreateMaintenanceRequestDTORequest dto = new CreateMaintenanceRequestDTORequest();
        dto.setAssetId(detail.assetId);
        
        String rawNote = detail.note;
        if (rawNote != null) {
            Pattern pattern = Pattern.compile("^\\[(.*?)\\] (.*?)(?: \\| Note: (.*))?$");
            Matcher matcher = pattern.matcher(rawNote);
            if (matcher.find()) {
                dto.setPriority(matcher.group(1));
                dto.setIssueDescription(matcher.group(2));
                String customNote = matcher.group(3);
                if (customNote != null) {
                    dto.setNote(customNote);
                }
            } else {
                // Fallback nếu không đúng chuẩn
                dto.setIssueDescription(rawNote);
                dto.setPriority("MEDIUM"); 
            }
        }

        return new MaintenanceRequestDetailResult(assetRequest.requestStatusId, dto, isOwner);
    }

    // Helper class to return both status and dto
    public static class MaintenanceRequestDetailResult {
        private String requestStatusId;
        private CreateMaintenanceRequestDTORequest dto;
        private boolean isOwner;

        public MaintenanceRequestDetailResult(String requestStatusId, CreateMaintenanceRequestDTORequest dto, boolean isOwner) {
            this.requestStatusId = requestStatusId;
            this.dto = dto;
            this.isOwner = isOwner;
        }

        public String getRequestStatusId() { return requestStatusId; }
        public CreateMaintenanceRequestDTORequest getDto() { return dto; }
        public boolean isOwner() { return isOwner; }
    }
}
