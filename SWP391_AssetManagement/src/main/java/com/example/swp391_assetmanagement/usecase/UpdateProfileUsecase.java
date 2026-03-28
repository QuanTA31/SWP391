package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.UpdateProfileDTORequest;
import com.example.swp391_assetmanagement.entity.UserDetail;
import com.example.swp391_assetmanagement.service.UserProfileService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class UpdateProfileUsecase {

    private final UserProfileService userProfileService;

    public void execute(UpdateProfileDTORequest request, HttpSession session) {
        String userCode = (String) session.getAttribute("USER_CODE");
        if (userCode == null) return;

        validateRequest(request);

        UserDetail existingDetail = userProfileService.viewProfile(userCode);
        if (existingDetail != null) {
            existingDetail.name = request.getName().trim();
            existingDetail.phone = request.getPhone().trim();
            existingDetail.email = request.getEmail().trim();
            existingDetail.dateOfBirth = request.getDateOfBirth();

            userProfileService.updateProfile(existingDetail);
        }
    }

    private void validateRequest(UpdateProfileDTORequest request) {
        // --- Name ---
        String name = request.getName();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Họ tên không được để trống.");
        }
        name = name.trim();
        if (name.length() < 2) {
            throw new IllegalArgumentException("Họ tên phải có ít nhất 2 ký tự.");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Họ tên không được vượt quá 100 ký tự.");
        }

        // --- Phone ---
        String phone = request.getPhone();
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống.");
        }
        phone = phone.trim();
        if (!phone.matches("^(0[35789])\\d{8}$")) {
            throw new IllegalArgumentException(
                "Số điện thoại không hợp lệ. Phải là 10 số và bắt đầu bằng 03, 05, 07, 08, hoặc 09.");
        }

        // --- Email ---
        String email = request.getEmail();
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email không được để trống.");
        }
        if (!email.trim().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("Địa chỉ email không đúng định dạng.");
        }

        // --- Date of Birth ---
        LocalDate dob = request.getDateOfBirth();
        if (dob == null) {
            throw new IllegalArgumentException("Ngày sinh không được để trống.");
        }
        LocalDate today = LocalDate.now();
        if (!dob.isBefore(today)) {
            throw new IllegalArgumentException(
                "Ngày sinh không được là ngày hôm nay hoặc trong tương lai.");
        }
        if (Period.between(dob, today).getYears() < 18) {
            throw new IllegalArgumentException("Bạn phải từ 18 tuổi trở lên.");
        }
    }
}
