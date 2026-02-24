package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.AssetAllProcessDAO;
import com.example.swp391_assetmanagement.service.AssetAllProcessService;
import com.example.swp391_assetmanagement.service.servicerequest.AllProcessServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.RequestProcessAllServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetAllProcessServiceImpl implements AssetAllProcessService {

    private final AssetAllProcessDAO assetAllProcessDAO;

    @Override
    public List<RequestProcessAllServiceResponse> viewAllProcess(AllProcessServiceRequest assetRequest) {
        return assetAllProcessDAO.selectRequestProcessAll(assetRequest);
    }

}
