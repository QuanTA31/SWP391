package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.service.servicerequest.ViewAssetByUserDisabledServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.ViewAssetByUserDisabledServiceResponse;

import java.util.List;

public interface ViewAssetByUserDisabledService {
    List<ViewAssetByUserDisabledServiceResponse> selectAllAssetByUserDisable(ViewAssetByUserDisabledServiceRequest request);
}
