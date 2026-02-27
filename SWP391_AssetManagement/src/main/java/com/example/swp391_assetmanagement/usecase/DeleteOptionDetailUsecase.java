package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.service.OptionDetailService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class DeleteOptionDetailUsecase {

    private final OptionDetailService optionDetailService;

    public void execute(Long optionDetailId, HttpSession session) {

        String role = (String) session.getAttribute("ROLE");
        if (!Roles.MANAGER.getValue().equals(role)
                && !Roles.PURCHASING.getValue().equals(role)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Bạn không có quyền truy cập chức năng này"
            );
        }
        optionDetailService.deleteById(optionDetailId);
    }
}