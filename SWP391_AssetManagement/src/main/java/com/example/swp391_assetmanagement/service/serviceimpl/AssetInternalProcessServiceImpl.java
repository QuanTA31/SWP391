package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.AssetInternalProcessDAO;
import com.example.swp391_assetmanagement.service.AssetInternalProcessService;
import com.example.swp391_assetmanagement.service.servicerequest.InternalProcessServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.InternalProcessAllServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetInternalProcessServiceImpl implements AssetInternalProcessService {

    private final AssetInternalProcessDAO assetInternalProcessDAO;

    @Override
    public List<InternalProcessAllServiceResponse> viewInternalProcess(InternalProcessServiceRequest assetRequest) {
        return assetInternalProcessDAO.selectInternalProcessAll(assetRequest);
    }
}
