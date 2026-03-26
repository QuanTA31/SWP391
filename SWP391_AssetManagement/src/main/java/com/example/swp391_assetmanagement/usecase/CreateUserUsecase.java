package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.CreateUserDTORequest;
import com.example.swp391_assetmanagement.dto.response.CreateUserDTOResponse;
import com.example.swp391_assetmanagement.entity.UserDetail;
import com.example.swp391_assetmanagement.entity.Users;
import com.example.swp391_assetmanagement.enums.Location;
import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.service.CreateUserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateUserUsecase {
    private final CreateUserService createUserService;

    @Transactional
    public CreateUserDTOResponse createUser(CreateUserDTORequest request) {

        long nextSeq = createUserService.countTotalUsers() + 1;
        String prefix = getPrefixByRole(request.getRoleId());
        String autoUserCode = String.format("%s%04d", prefix, nextSeq);

        // 3. Chuẩn bị Entity Users
        Users userEntity = new Users();
        userEntity.setUserCode(autoUserCode);
        userEntity.setUsername(request.getUsername());
        String hashedPass = encryptSHA1(request.getPassword());
        userEntity.setPassword(hashedPass);
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
        return CreateUserDTOResponse.builder()
                .userCode(userEntity.userCode)
                .username(userEntity.username)
                .name(detailEntity.name)
                .email(detailEntity.email)
                .roleId(userEntity.roleId)
                .statusId(userEntity.statusId)
                .locationId(detailEntity.locationId)
                .phone(detailEntity.phone)
                .dateOfBirth(detailEntity.dateOfBirth)
                .password(userEntity.password)
                .build();
    }

    public static String encryptSHA1(String input) {
        try {
            // Khởi tạo đối tượng MessageDigest với thuật toán SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // Thực hiện mã hóa chuỗi đầu vào (trả về mảng byte)
            byte[] messageDigest = md.digest(input.getBytes());

            // Chuyển đổi mảng byte sang định dạng Hexadecimal (thập lục phân)
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Not found SHA-1", e);
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
}
