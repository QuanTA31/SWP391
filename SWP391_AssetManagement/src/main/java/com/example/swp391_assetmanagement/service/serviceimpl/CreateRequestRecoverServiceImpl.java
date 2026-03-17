package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.CreateRequestRecoverDAO;
import com.example.swp391_assetmanagement.dao.UserDAO;
import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.Assets;
import com.example.swp391_assetmanagement.service.CreateRequestRecoverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateRequestRecoverServiceImpl implements CreateRequestRecoverService {

    private final CreateRequestRecoverDAO dao;
    private final UserDAO userDAO;

    @Override
    public Long createRequestRecover(AssetRequest request) {
        dao.insertRequestRecover(request);
        return request.id;
    }

    @Override
    public void createRequestInternalRecover(AssetInternalRequestDetail detail) {
        dao.inrsertAssetRecoverToSigleRequest(detail);
    }

    @Override
    public List<Assets> getAssetsByCodes(List<String> assetCodes) {
        return dao.selectAssetsByCodes(assetCodes);
    }

    @Override
    public Long getUserIdByCode(String userCode) {
        return userDAO.findIdByUserCode(userCode);
    }

    @Override
    public void updateAssetsToRetrieval(List<Long> assetIds) {
        dao.updateAssetStatusToRetrival(assetIds);
    }
}
