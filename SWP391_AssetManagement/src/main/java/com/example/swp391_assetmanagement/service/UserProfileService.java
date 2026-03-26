package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.entity.UserDetail;

public interface UserProfileService {
    UserDetail viewProfile(String userCode);

    int updateProfile(UserDetail userDetail);
}
