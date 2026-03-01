package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.AssetSequencesDAO;
import com.example.swp391_assetmanagement.entity.AssetSequences;
import com.example.swp391_assetmanagement.service.AssetSequencesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssetSequencesServiceImpl implements AssetSequencesService {

    private final AssetSequencesDAO assetSequencesDAO;

    @Override
    public AssetSequences findByIdToUpdate(String assetType) {
        return assetSequencesDAO.findByIdToUpdate(assetType);
    }

    @Override
    public int update(AssetSequences assetSequences) {
        return assetSequencesDAO.update(assetSequences);
    }

}
