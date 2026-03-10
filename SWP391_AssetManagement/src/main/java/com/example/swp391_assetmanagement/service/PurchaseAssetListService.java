package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.service.servicerequest.PurchaseAssetAllServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.PurchaseAssetAllServiceResponse;

import java.util.List;

public interface PurchaseAssetListService {

    List<PurchaseAssetAllServiceResponse> viewPurchaseAssetList(PurchaseAssetAllServiceRequest purchaseAssetAllServiceRequest);

}
