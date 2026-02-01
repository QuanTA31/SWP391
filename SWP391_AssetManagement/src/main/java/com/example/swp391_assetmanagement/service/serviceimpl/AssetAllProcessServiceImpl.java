package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.AssetAllProcessDAO;
import com.example.swp391_assetmanagement.dao.AssetInternalProcessDAO;
import com.example.swp391_assetmanagement.service.AssetAllProcessService;
import com.example.swp391_assetmanagement.service.servicerequest.AllProcessRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.RequestProcessAllResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetAllProcessServiceImpl implements AssetAllProcessService {

    private final AssetAllProcessDAO assetAllProcessDAO;

    @Override
    public List<RequestProcessAllResponse> viewAllProcess(AllProcessRequest assetRequest) {
        return assetAllProcessDAO.selectRequestProcessAll(assetRequest);
    }

}
