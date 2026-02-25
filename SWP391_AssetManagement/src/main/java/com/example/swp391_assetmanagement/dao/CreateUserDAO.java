package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.service.servicerequest.CreateUserServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.CreateUserServiceResponse;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.Optional;

@Dao
@ConfigAutowireable
public interface CreateUserDAO {

    @Insert
    Optional<CreateUserServiceResponse> User(CreateUserServiceRequest request);
}
