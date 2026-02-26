package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.service.servicerequest.AllProcessServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.RequestProcessAllServiceResponse;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

@Dao
@ConfigAutowireable
public interface AssetAllProcessDAO {
    @Select
    List<RequestProcessAllServiceResponse> selectRequestProcessAll(AllProcessServiceRequest request);

}
