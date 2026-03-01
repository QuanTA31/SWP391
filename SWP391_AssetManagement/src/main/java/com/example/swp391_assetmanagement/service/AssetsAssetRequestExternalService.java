package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.entity.AssetsAssetRequestExternal;

import java.util.List;

public interface AssetsAssetRequestExternalService {
    int[] batchInsert(List<AssetsAssetRequestExternal> assetsAssetRequestExternals);
}

