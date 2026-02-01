package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.service.servicerequest.AllProcessRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.RequestProcessAllResponse;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

@Dao
@ConfigAutowireable
public interface AssetAllProcessDAO {

    @Select
    List<RequestProcessAllResponse> selectRequestProcessAll(AllProcessRequest request);

}
