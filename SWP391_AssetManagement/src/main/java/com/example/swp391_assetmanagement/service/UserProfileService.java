package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.entity.UserDetail;
import com.example.swp391_assetmanagement.entity.Users;

public interface UserProfileService {
    UserDetail viewProfile(String userCode);
    int updateProfile(UserDetail userDetail);
    Users findUserById(Long userId);
    int updateUserPassword(Users user);
    Long findIdByUserCode(String userCode);
}
