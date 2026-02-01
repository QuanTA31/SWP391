package com.example.swp391_assetmanagement.service.serviceimpl;


import com.example.swp391_assetmanagement.dao.AssetExternalProcessDAO;
import com.example.swp391_assetmanagement.service.AssetExternalProcessService;
import com.example.swp391_assetmanagement.service.servicerequest.ExternalProcessRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.ExternalProcessAllResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetExternalProcessServiceImpl implements AssetExternalProcessService {

    private final AssetExternalProcessDAO assetExternalProcessDAO;

    @Override
    public List<ExternalProcessAllResponse> viewExternalProcess(ExternalProcessRequest assetRequest) {
        return assetExternalProcessDAO.selectExternalProcessAll(assetRequest);
    }

}
