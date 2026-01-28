package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.AssetInternalProcessDAO;
import com.example.swp391_assetmanagement.service.AssetInternalProcessService;
import com.example.swp391_assetmanagement.service.servicerequest.InternalProcessRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.InternalProcessAllResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetInternalProcessServiceImpl implements AssetInternalProcessService {

    private final AssetInternalProcessDAO assetInternalProcessDAO;

    @Override
    public List<InternalProcessAllResponse> viewInternalProcess(InternalProcessRequest assetRequest) {
        return assetInternalProcessDAO.selectAssetAll(assetRequest);
    }
}
