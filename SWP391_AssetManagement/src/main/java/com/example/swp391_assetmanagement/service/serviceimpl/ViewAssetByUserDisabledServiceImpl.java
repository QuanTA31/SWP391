package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.ViewAssetByUserDisabledDAO;
import com.example.swp391_assetmanagement.service.ViewAssetByUserDisabledService;
import com.example.swp391_assetmanagement.service.servicerequest.ViewAssetByUserDisabledServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.ViewAssetByUserDisabledServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ViewAssetByUserDisabledServiceImpl implements ViewAssetByUserDisabledService {

    private final ViewAssetByUserDisabledDAO viewAssetByUserDisabledDAO;

    @Override
    public List<ViewAssetByUserDisabledServiceResponse> selectAllAssetByUserDisable(ViewAssetByUserDisabledServiceRequest request) {
        return viewAssetByUserDisabledDAO.selectAssetByUserDisabled(request);
    }
}
