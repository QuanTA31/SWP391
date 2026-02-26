package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.service.servicerequest.ExternalProcessServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.ExternalProcessAllServiceResponse;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

@Dao
@ConfigAutowireable
public interface AssetExternalProcessDAO {

    @Select
    List<ExternalProcessAllServiceResponse> selectExternalProcessAll(ExternalProcessServiceRequest request);

}
