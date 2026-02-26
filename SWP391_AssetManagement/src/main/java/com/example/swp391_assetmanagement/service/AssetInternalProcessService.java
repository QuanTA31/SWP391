package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.service.servicerequest.InternalProcessServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.InternalProcessAllServiceResponse;

import java.util.List;

public interface AssetInternalProcessService {

    List<InternalProcessAllServiceResponse> viewInternalProcess(InternalProcessServiceRequest assetRequest);
}
