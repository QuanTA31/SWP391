package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.UserDAO;
import com.example.swp391_assetmanagement.entity.UserDetail;
import com.example.swp391_assetmanagement.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserDAO userDAO;

    @Override
    public UserDetail viewProfile(String userCode) {
        Long userId = userDAO.findIdByUserCode(userCode);
        return userDAO.findUserDetailByUserId(userId);
    }

    @Override
    public int updateProfile(UserDetail userDetail) {
        return userDAO.updateUserDetail(userDetail);
    }
}
