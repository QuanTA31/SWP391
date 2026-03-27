package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.ViewAssetToRetrievalDAO;
import com.example.swp391_assetmanagement.service.ViewAssetToRetrievalService;
import com.example.swp391_assetmanagement.service.servicerequest.ViewAssetToRetrievalServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.ViewAssetToRetrievalServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ViewAssetToRetrievalServiceImpl implements ViewAssetToRetrievalService {

    private final ViewAssetToRetrievalDAO viewAssetToRetrievalDAO;

    @Override
    public List<ViewAssetToRetrievalServiceResponse> selectAllAssetToRetrieval(ViewAssetToRetrievalServiceRequest request) {
        return viewAssetToRetrievalDAO.selectAssetToRetrieval(request);
    }
}
