package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.CreateUserDTORequest;
import com.example.swp391_assetmanagement.entity.UserDetail;
import com.example.swp391_assetmanagement.entity.Users;
import com.example.swp391_assetmanagement.enums.Location;
import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.service.CreateUserService;
import com.example.swp391_assetmanagement.service.servicerequest.CreateUserServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.CreateUserServiceResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateUserUsecase {
    private final CreateUserService createUserService;

    @Transactional
    public CreateUserDTORequest createUser(CreateUserServiceRequest request, HttpSession session) {
        // 1. Validate (Tương tự ViewAllUserUsecase)
        validateCreateRequest(request, session);

        // 2. Logic tạo User Code tự động
        long nextSeq = createUserService.countTotalUsers() + 1;
        String prefix = getPrefixByRole(request.getRoleId());
        String autoUserCode = String.format("%s%04d", prefix, nextSeq);

        // 3. Chuẩn bị Entity Users
        Users userEntity = new Users();
        userEntity.setUserCode(autoUserCode);
        userEntity.setUsername(request.getUsername());
        userEntity.setPassword(request.getPassword()); // Nên mã hóa password ở đây
        userEntity.setRoleId(request.getRoleId());
        userEntity.setStatusId(request.getStatusId());
        userEntity.setCreatedAt(LocalDateTime.now());

        // 4. Chuẩn bị Entity UserDetail
        UserDetail detailEntity = new UserDetail();
        detailEntity.setName(request.getName());
        detailEntity.setPhone(request.getPhone());
        detailEntity.setEmail(request.getEmail());
        detailEntity.setDateOfBirth(request.getDateOfBirth());
        detailEntity.setLocationId(request.getLocationId());
        detailEntity.setCreatedAt(LocalDateTime.now());

        // 5. Gọi Service để lưu vào DB
        createUserService.saveUser(userEntity, detailEntity);

        // 6. Map to Response (Hàm map thủ công như đã giải thích)
        return CreateUserDTORequest.builder()
                .userCode(userEntity.getUserCode())
                .username(userEntity.getUsername())
                .name(detailEntity.getName())
                .email(detailEntity.getEmail())
                .roleId(userEntity.getRoleId())
                .statusId(userEntity.getStatusId())
                .locationId(detailEntity.getLocationId())
                .phone(detailEntity.getPhone())
                .dateOfBirth(detailEntity.getDateOfBirth())
                .password(userEntity.getPassword())
                .build();
//                mapToResponse(userEntity, detailEntity);
    }

    private void validateCreateRequest(CreateUserServiceRequest request, HttpSession session) {
        // Kiểm tra quyền (Chỉ ADMIN/MANAGER mới được tạo user)
        String role = (String) session.getAttribute("ROLE");
        if (!Roles.ADMIN.getValue().equals(role) && !Roles.MANAGER.getValue().equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền thực hiện thao tác này!");
        }

        // Validate Enums
        if (!Location.hasValue(request.getLocationId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location invalid!");
        }
    }

    private String getPrefixByRole(String roleId) {
        return switch (roleId) {
            case "01" -> "A";
            case "02" -> "M";
            case "03" -> "W";
            case "04" -> "P";
            case "05" -> "D";
            default -> "C";
        };
    }

//    private CreateUserServiceResponse mapToResponse(Users u, UserDetail d) {
//        // Sử dụng Builder nếu bạn đã thêm @Builder vào Response
//        return CreateUserDTORequest.builder()
//                .userCode(u.getUserCode())
//                .username(u.getUsername())
//                .name(d.getName())
//                .email(d.getEmail())
//                .roleId(u.getRoleId())
//                .statusId(u.getStatusId())
//                .locationId(d.getLocationId())
//                .build();
//    }
}
