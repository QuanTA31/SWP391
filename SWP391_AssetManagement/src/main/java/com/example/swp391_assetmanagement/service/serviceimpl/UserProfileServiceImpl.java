package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.UserDAO;
import com.example.swp391_assetmanagement.entity.UserDetail;
import com.example.swp391_assetmanagement.entity.Users;
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

    @Override
    public Users findUserById(Long userId) {
        return userDAO.findUserById(userId);
    }

    @Override
    public int updateUserPassword(Users user) {
        return userDAO.updateUserPassword(user);
    }

    @Override
    public Long findIdByUserCode(String userCode) {
        return userDAO.findIdByUserCode(userCode);
    }
}
