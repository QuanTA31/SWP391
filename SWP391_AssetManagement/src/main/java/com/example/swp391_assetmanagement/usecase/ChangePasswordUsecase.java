package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.ChangePasswordDTORequest;
import com.example.swp391_assetmanagement.entity.Users;
import com.example.swp391_assetmanagement.service.UserProfileService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChangePasswordUsecase {

    private final UserProfileService userProfileService;

    @Transactional
    public void execute(ChangePasswordDTORequest request, HttpSession session) {
        String userCode = (String) session.getAttribute("USER_CODE");
        if (userCode == null) {
            throw new IllegalArgumentException("User not authenticated.");
        }

        Long userId = userProfileService.findIdByUserCode(userCode);
        if (userId == null) {
            throw new IllegalArgumentException("User ID not found.");
        }

        Users user = userProfileService.findUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        if (request.getOldPassword() == null || request.getNewPassword() == null || request.getConfirmPassword() == null) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin.");
        }

        String hashedOldPassword = LoginUsecase.encryptSHA1(request.getOldPassword());
        if (!hashedOldPassword.equals(user.password)) {
            throw new IllegalArgumentException("Mật khẩu cũ không chính xác.");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Mật khẩu xác nhận không khớp.");
        }

        user.setPassword(LoginUsecase.encryptSHA1(request.getNewPassword()));
        userProfileService.updateUserPassword(user);
    }
}
