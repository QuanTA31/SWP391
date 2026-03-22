package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.service.servicerequest.RecoverServiceRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AssetRequestService {

    String findRequestTypeById(Long assetRequestId);

    Long createPurchaseRequestForm(AssetRequest assetRequest);

    Optional<AssetRequest> findAssetRequestByIdForUpdate(Long assetRequestId);

    void updatePurchaseRequest(AssetRequest assetRequest);

    void updateLiquidationRequest(AssetRequest assetRequest);

    void updatePurchaseRequestStatus(AssetRequest assetRequest);

    Integer countById(Long assetRequestId, String status);

    AssetRequest findByUpdate(Long assetRequestId);

    int updateIsSelected(AssetRequest assetRequest);

    Long findIdByAssetRequestDetailId(Long assetRequestDetailId);

    int moveInProgress(Long requestId);

    // Retrival
    //find assetRequest by id
    AssetRequest findById(Long id);
    //find detail by request Id
    List<AssetInternalRequestDetail> findDetailsByRequestId(Long requestId);
    // main to comfirm the request detail to is done and fix information asset to null user
    void confirmDetailAndRestoreAsset(RecoverServiceRequest serviceRequest);
    //get status id by request id
    String getRequestStatusById(Long requestId);
    //fix the status request
    void updateRequestStatus(Long requestId, String statusId);
    //if all request detail is done set request to complete or if warehouse click on the button in screen set it to in progres
    boolean isAllDetailsDone(Long requestId);

    void updateHandoverDate(Long requestId, LocalDate handoverDate);

    int moveCompleted(Long requestId);
}
