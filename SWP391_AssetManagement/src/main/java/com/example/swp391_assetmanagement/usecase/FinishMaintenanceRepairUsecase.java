package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dao.AssetRequestDAO;
import com.example.swp391_assetmanagement.dao.AssetsDAO;
import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.Assets;
import com.example.swp391_assetmanagement.enums.AssetStatus;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.service.AssetInternalRequestDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class FinishMaintenanceRepairUsecase {

    private final AssetRequestDAO assetRequestDAO;
    private final AssetsDAO assetsDAO;
    private final AssetInternalRequestDetailService assetInternalRequestDetailService;

    @Transactional
    public void execute(Long assetRequestId, boolean isSuccess) {
        
        AssetRequest assetRequest = assetRequestDAO.findAssetRequestByIdForUpdate(assetRequestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy yêu cầu"));

        if (!RequestStatus.IN_PROGRESS.getValue().equals(assetRequest.requestStatusId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trạng thái yêu cầu không hợp lệ để hoàn tất sửa chữa.");
        }

        AssetInternalRequestDetail detail = assetInternalRequestDetailService.findByAssetRequestId(assetRequestId);
        if (detail == null || detail.assetId == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dữ liệu chi tiết bị lỗi");
        }

        Assets asset = assetsDAO.findById(detail.assetId);
        if (asset == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy tài sản");
        }

        if (isSuccess) {
            // "Sửa OK" => Request status = MAINTAIN_DONE
            assetRequest.requestStatusId = RequestStatus.MAINTAIN_DONE.getValue();
        } else {
            // "Không sửa được" => Asset status = BROKEN, Request status = COMPLETED
            asset.assetStatusId = AssetStatus.BROKEN.getValue();
            assetsDAO.update(asset);
            assetRequest.requestStatusId = RequestStatus.COMPLETED.getValue();
        }

        // Cập nhật lại request_status_id
        assetRequestDAO.updateStatus(assetRequest);
    }
}
