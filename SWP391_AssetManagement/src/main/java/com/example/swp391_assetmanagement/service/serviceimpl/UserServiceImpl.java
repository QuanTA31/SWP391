package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.UserDAO;
import com.example.swp391_assetmanagement.service.UserService;
import com.example.swp391_assetmanagement.service.servicerequest.LoginServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.LocationViewAssetServiceResponse;
import com.example.swp391_assetmanagement.service.serviceresponse.LoginServiceResponse;
import com.example.swp391_assetmanagement.service.serviceresponse.UserDropdownResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    @Override
    public LoginServiceResponse authenticate(LoginServiceRequest request) {

        Optional<LoginServiceResponse> userDAOResponse = userDAO.findByUsername(request);
        if (userDAOResponse.isPresent()) {
            return userDAOResponse.get();
        } else {
            return null;
        }
    }

    @Override
    public LocationViewAssetServiceResponse getLocationViewAsset(String userCode) {
        return userDAO.findLocationByAssetCode(userCode);
    }

    @Override
    public Long getIdByUserCode(String userCode) {
        return userDAO.findIdByUserCode(userCode);
    }

    @Override
    public String getRoleIdByUserCode(String userCode) {
        return userDAO.findRoleCodesByUserCode(userCode);
    }

    @Override
    public List<UserDropdownResponse> getActiveUsersByLocation(String locationId) {
        return userDAO.findUsersByLocationId(locationId);
    }

    @Override
    public String getUserNameById(Long id) {
        return userDAO.findUserNameById(id);
    }
}
