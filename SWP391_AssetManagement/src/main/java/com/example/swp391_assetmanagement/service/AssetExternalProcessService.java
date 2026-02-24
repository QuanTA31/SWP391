package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.service.servicerequest.ExternalProcessServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.ExternalProcessAllServiceResponse;

import java.util.List;

public interface AssetExternalProcessService {

    List<ExternalProcessAllServiceResponse> viewExternalProcess(ExternalProcessServiceRequest assetRequest);

}
