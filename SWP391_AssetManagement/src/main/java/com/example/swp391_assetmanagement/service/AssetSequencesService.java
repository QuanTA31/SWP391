package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.entity.AssetSequences;

public interface AssetSequencesService {

    AssetSequences findByIdToUpdate(String assetType);

    int update(AssetSequences assetSequences);
}
