package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.response.ViewProfileDTOResponse;
import com.example.swp391_assetmanagement.entity.UserDetail;
import com.example.swp391_assetmanagement.service.UserProfileService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViewProfileUsecase {
    
    private final UserProfileService userProfileService;

    public ViewProfileDTOResponse execute(HttpSession session) {
        String userCode = (String) session.getAttribute("USER_CODE");
        if (userCode == null) return null;

        UserDetail userDetail = userProfileService.viewProfile(userCode);
        String username = (String) session.getAttribute("USER_NAME");

        if (userDetail == null) {
            return ViewProfileDTOResponse.builder()
                    .username(username)
                    .build();
        }

        return ViewProfileDTOResponse.builder()
                .userId(userDetail.userId)
                .name(userDetail.name)
                .phone(userDetail.phone)
                .email(userDetail.email)
                .dateOfBirth(userDetail.dateOfBirth)
                .locationId(userDetail.locationId)
                .username(username)
                .build();
    }
}
