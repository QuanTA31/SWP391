package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.service.servicerequest.InternalProcessRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.InternalProcessAllResponse;

import java.util.List;

public interface AssetInternalProcessService {

    List<InternalProcessAllResponse> viewInternalProcess(InternalProcessRequest assetRequest);
}
