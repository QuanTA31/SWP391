package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.service.servicerequest.ViewAssetByUserDisabledServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.ViewAssetByUserDisabledServiceResponse;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

@Dao
@ConfigAutowireable
public interface ViewAssetByUserDisabledDAO {
    @Select
    List<ViewAssetByUserDisabledServiceResponse> selectAssetByUserDisabled(ViewAssetByUserDisabledServiceRequest request);
}
