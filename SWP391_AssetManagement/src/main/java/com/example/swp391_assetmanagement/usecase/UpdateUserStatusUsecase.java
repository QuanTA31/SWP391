package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.Users;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.service.UpdateUserStatusService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UpdateUserStatusUsecase {
    private final UpdateUserStatusService updateUserStatusService;
    // Giả sử bạn cần DAO hoặc Service để tìm User cũ trước khi update
    // private final UserDAO userDAO;

    public void execute(String username, String status, HttpSession session) {
        // 1. Khởi tạo object Users với dữ liệu mới
        // Lưu ý: Doma @Update thường yêu cầu object phải có thông tin Primary Key (username) và Version (nếu có)
        Users user = new Users();
        user.setUsername(username);
        user.setStatusId(status);

        // 2. Gọi service để update
        int count = updateUserStatusService.UpdateStatus(user);

        // 3. Kiểm tra kết quả
        if (count == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found or update failed!");
        }
    }
}
