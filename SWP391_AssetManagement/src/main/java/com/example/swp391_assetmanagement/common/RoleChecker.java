package com.example.swp391_assetmanagement.common;

import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class RoleChecker {

    private final UserService userService;

    public void requireRole(String userCode, Roles... roles) {

        //check user tồn tại
        if (userCode == null || userCode.isBlank()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        //lấy role id qua user code
        String roleCode = userService.getRoleCodesByUserCode(userCode);

        //check role null
        if (roleCode == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        //check role tồn tại trong db
        for (Roles role : roles) {
            if (role.name().equals(roleCode)) {
                return;
            }
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
}