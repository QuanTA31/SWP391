package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.service.servicerequest.ViewAssetToRetrievalServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.ViewAssetToRetrievalServiceResponse;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

@Dao
@ConfigAutowireable
public interface ViewAssetToRetrievalDAO {
    @Select
    List<ViewAssetToRetrievalServiceResponse> selectAssetToRetrieval(ViewAssetToRetrievalServiceRequest request);
}
