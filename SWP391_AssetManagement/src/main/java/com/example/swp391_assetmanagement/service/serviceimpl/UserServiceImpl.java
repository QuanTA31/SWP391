package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.UserDAO;
import com.example.swp391_assetmanagement.service.UserService;
import com.example.swp391_assetmanagement.dto.request.LoginRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.UserDAOResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    @Override
    public UserDAOResponse authenticate(LoginRequest request){
        UserDAOResponse userDAOResponse = userDAO.findByUsername(request);
        if(userDAOResponse != null){
            return userDAOResponse;
        }
        return null;
    }
}
