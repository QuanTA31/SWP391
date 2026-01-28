package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.daoresponse.AssetsDaoResponse;
import com.example.swp391_assetmanagement.service.AssetService;
import com.example.swp391_assetmanagement.service.servicerequest.AssetRequest;
import com.example.swp391_assetmanagement.service.servicerequest.LoginRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetResponse;
import com.example.swp391_assetmanagement.dao.AssetsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetServiceImpl implements AssetService {

    private final AssetsDAO assetsDAO;

    @Autowired
    public AssetServiceImpl(AssetsDAO assetsDAO) {
        this.assetsDAO = assetsDAO;
    }

    @Override
    public AssetResponse createAsset(AssetRequest assetRequest) {
        AssetsDaoResponse specialEntity = assetsDAO.selectById(assetRequest.getId());
        return AssetResponse.builder()
                .assetCode(specialEntity.assetCode)
                .description(specialEntity.description)
                .build();
    }

    @Autowired
    public AssetsDAO assetDAO;

    @Override
    public boolean authenticate(LoginRequest request){

    }
}
