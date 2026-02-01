package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.service.servicerequest.ExternalProcessRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.ExternalProcessAllResponse;

import java.util.List;

public interface AssetExternalProcessService {

    List<ExternalProcessAllResponse> viewExternalProcess(ExternalProcessRequest assetRequest);

}
