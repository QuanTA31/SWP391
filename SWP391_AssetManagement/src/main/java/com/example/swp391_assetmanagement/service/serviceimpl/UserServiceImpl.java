package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.UserDAO;
import com.example.swp391_assetmanagement.service.UserService;
import com.example.swp391_assetmanagement.service.servicerequest.UserLoginRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.LocationViewAssetResponse;
import com.example.swp391_assetmanagement.service.serviceresponse.UserLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    @Override
    public UserLoginResponse authenticate(UserLoginRequest request){

        UserLoginResponse userDAOResponse = userDAO.findByUsername(request);
        if(!ObjectUtils.isEmpty(userDAOResponse)){
            return userDAOResponse;
        }
        return null;
    }

    @Override
    public LocationViewAssetResponse getLocationViewAsset(String userCode) {
        return userDAO.findLocationByAssetCode(userCode);
    }
}
