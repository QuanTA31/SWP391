package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.service.servicerequest.ViewAssetToRetrievalServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.ViewAssetToRetrievalServiceResponse;

import java.util.List;

public interface ViewAssetToRetrievalService {
    List<ViewAssetToRetrievalServiceResponse> selectAllAssetToRetrieval(ViewAssetToRetrievalServiceRequest request);
}
