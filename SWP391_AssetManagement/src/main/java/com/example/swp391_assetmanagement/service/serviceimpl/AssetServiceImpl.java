package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.service.AssetService;
import com.example.swp391_assetmanagement.service.servicerequest.AssetRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetResponse;
import com.example.swp391_assetmanagement.service.specialrepository.AssetSpecialEntity;
import com.example.swp391_assetmanagement.service.specialrepository.AssetSpecialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetServiceImpl implements AssetService {

    private final AssetSpecialRepository assetSpecialRepository;

    @Autowired
    public AssetServiceImpl(AssetSpecialRepository assetSpecialRepository) {
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
