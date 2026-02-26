package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.entity.UserDetail;
import com.example.swp391_assetmanagement.entity.Users;
import com.example.swp391_assetmanagement.service.servicerequest.CreateUserServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.CreateUserServiceResponse;

public interface CreateUserService {

    long countTotalUsers();

    void saveUser(Users user, UserDetail detail);
}
