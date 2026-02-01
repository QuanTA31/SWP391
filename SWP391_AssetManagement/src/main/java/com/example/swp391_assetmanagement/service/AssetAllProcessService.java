package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.service.servicerequest.AllProcessRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.RequestProcessAllResponse;

import java.util.List;

public interface AssetAllProcessService {
    List<RequestProcessAllResponse> viewAllProcess(AllProcessRequest assetRequest);
}
