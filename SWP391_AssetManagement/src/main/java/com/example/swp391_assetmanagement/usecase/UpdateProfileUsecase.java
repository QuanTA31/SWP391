package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.UpdateProfileDTORequest;
import com.example.swp391_assetmanagement.entity.UserDetail;
import com.example.swp391_assetmanagement.service.UserProfileService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateProfileUsecase {

    private final UserProfileService userProfileService;

    public void execute(UpdateProfileDTORequest updateRequest, HttpSession session) {
        String userCode = (String) session.getAttribute("USER_CODE");
        if (userCode == null) return;

        if (updateRequest.getDateOfBirth() == null) {
            throw new IllegalArgumentException("Date of birth cannot be null");
        }

        UserDetail existingDetail = userProfileService.viewProfile(userCode);
        if (existingDetail != null) {
            existingDetail.name = updateRequest.getName();
            existingDetail.phone = updateRequest.getPhone();
            existingDetail.email = updateRequest.getEmail();
            existingDetail.dateOfBirth = updateRequest.getDateOfBirth();
            
            userProfileService.updateProfile(existingDetail);
        }
    }
}
