package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dao.AssetRequestDAO;
import com.example.swp391_assetmanagement.dto.request.CreateMaintenanceRequestDTORequest;
import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.Assets;
import com.example.swp391_assetmanagement.dao.AssetsDAO;
import com.example.swp391_assetmanagement.dao.UserDAO;
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
    private final AssetsDAO assetsDAO;
    private final UserDAO userDAO;

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
                dto.setIssueDescription(rawNote);
                dto.setPriority("MEDIUM"); 
            }
        }

        Assets asset = detail.assetId != null ? assetsDAO.findById(detail.assetId) : null;
        String requesterName = assetRequest.requestedBy != null ? userDAO.findUserNameById(assetRequest.requestedBy) : null;

        return new MaintenanceRequestDetailResult(assetRequest.requestStatusId, dto, isOwner, assetRequest, asset, requesterName);
    }

    public static class MaintenanceRequestDetailResult {
        private String requestStatusId;
        private CreateMaintenanceRequestDTORequest dto;
        private boolean isOwner;
        private AssetRequest assetRequest;
        private Assets asset;
        private String requesterName;

        public MaintenanceRequestDetailResult(String requestStatusId, CreateMaintenanceRequestDTORequest dto, boolean isOwner, AssetRequest assetRequest, Assets asset, String requesterName) {
            this.requestStatusId = requestStatusId;
            this.dto = dto;
            this.isOwner = isOwner;
            this.assetRequest = assetRequest;
            this.asset = asset;
            this.requesterName = requesterName;
        }

        public String getRequestStatusId() { return requestStatusId; }
        public CreateMaintenanceRequestDTORequest getDto() { return dto; }
        public boolean isOwner() { return isOwner; }
        public AssetRequest getAssetRequest() { return assetRequest; }
        public Assets getAsset() { return asset; }
        public String getRequesterName() { return requesterName; }
    }
}
