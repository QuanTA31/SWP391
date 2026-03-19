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
public class ConfirmMaintenanceReceiptUsecase {

    private final AssetRequestDAO assetRequestDAO;
    private final AssetInternalRequestDetailService assetInternalRequestDetailService;
    private final AssetsDAO assetsDAO;

    @Transactional
    public void execute(Long requestId) {
        AssetRequest assetRequest = assetRequestDAO.findAssetRequestByIdForUpdate(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy yêu cầu"));

        if (!RequestStatus.MAINTAIN_DONE.getValue().equals(assetRequest.requestStatusId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Yêu cầu phải ở trạng thái Sửa xong mới có thể xác nhận nhận lại");
        }

        AssetInternalRequestDetail detail = assetInternalRequestDetailService.findByAssetRequestId(requestId);
        if (detail == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy chi tiết yêu cầu");
        }

        Assets asset = assetsDAO.findById(detail.assetId);
        if (asset == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy tài sản");
        }

        asset.assetStatusId = AssetStatus.ASSIGNED.getValue();
        asset.locationId = detail.fromLocationId;
        assetsDAO.update(asset);

        assetRequest.requestStatusId = RequestStatus.COMPLETED.getValue();
        assetRequestDAO.updateStatus(assetRequest);
    }
}
