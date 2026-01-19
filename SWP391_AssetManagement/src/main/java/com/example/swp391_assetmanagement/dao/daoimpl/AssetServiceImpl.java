package com.example.swp391_assetmanagement.dao.daoimpl;

import com.example.swp391_assetmanagement.service.AssetService;
import com.example.swp391_assetmanagement.service.servicerequest.AssetRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetResponse;
import com.example.swp391_assetmanagement.service.specialrepository.AssetSpecialEntity;
import com.example.swp391_assetmanagement.dao.AssetsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetServiceImpl implements AssetService {

    private final AssetsDAO assetSpecialRepository;

    @Autowired
    public AssetServiceImpl(AssetsDAO assetSpecialRepository) {
        this.assetSpecialRepository = assetSpecialRepository;
    }

    @Override
    public AssetResponse createAsset(AssetRequest assetRequest) {
        AssetSpecialEntity specialEntity = assetSpecialRepository.selectById(assetRequest);
        return AssetResponse.builder()
                .assetCode(specialEntity.assetCode)
                .description(specialEntity.description)
                .build();
    }
}
