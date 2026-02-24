package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.service.servicerequest.InternalProcessServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.InternalProcessAllServiceResponse;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

@Dao
@ConfigAutowireable
public interface AssetInternalProcessDAO {

    @Select
    List<InternalProcessAllServiceResponse> selectInternalProcessAll(InternalProcessServiceRequest request);
}
