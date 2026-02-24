package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.service.servicerequest.AllProcessServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.RequestProcessAllServiceResponse;

import java.util.List;

public interface AssetAllProcessService {
    List<RequestProcessAllServiceResponse> viewAllProcess(AllProcessServiceRequest assetRequest);
}
