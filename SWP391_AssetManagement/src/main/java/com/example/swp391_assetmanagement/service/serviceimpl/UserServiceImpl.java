package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.UserDAO;
import com.example.swp391_assetmanagement.service.UserService;
import com.example.swp391_assetmanagement.service.servicerequest.LoginRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.LocationViewAssetResponse;
import com.example.swp391_assetmanagement.service.serviceresponse.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    @Override
    public LoginResponse authenticate(LoginRequest request){

        Optional<LoginResponse> userDAOResponse = userDAO.findByUsername(request);
        if(userDAOResponse.isPresent()){
            return userDAOResponse.get();
        }
        return null;
    }

    @Override
    public LocationViewAssetResponse getLocationViewAsset(String userCode) {
        return userDAO.findLocationByAssetCode(userCode);
    }
}
