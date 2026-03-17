package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.LoginDTORequest;
import com.example.swp391_assetmanagement.dto.response.LoginDTOResponse;
import com.example.swp391_assetmanagement.service.UserService;
import com.example.swp391_assetmanagement.service.servicerequest.LoginServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.LoginServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class LoginUsecase {

    private final UserService userService;

    @Transactional(readOnly = true)
    public LoginDTOResponse executeLogin(LoginDTORequest request) {

        LoginServiceResponse loginResponse =  userService.authenticate(LoginServiceRequest.builder()
                        .username(request.getUsername())
                        .password(encryptSHA1(request.getPassword()))
                .build());

        if(loginResponse != null) {
            return LoginDTOResponse.builder()
                    .userId(loginResponse.getId())
                    .userName(loginResponse.getUsername())
                    .roleId(loginResponse.getRoleId())
                    .userCode(loginResponse.getUserCode())
                    .locationId(loginResponse.getLocationId())
                    .build();
        }else {
            return null;
        }
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
            throw new RuntimeException("Không tìm thấy thuật toán mã hóa SHA-1", e);
        }
    }
}
