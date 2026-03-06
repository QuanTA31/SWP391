package com.example.swp391_assetmanagement.common;

import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class RoleChecker {

    private final UserService userService;

    public void requireRole(String userCode, Roles... roles) {

        //check user tồn tại
        if (ObjectUtils.isEmpty(userCode)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        //lấy role id qua user code
        String roleCode = userService.getRoleCodesByUserCode(userCode);

        //check role null
        if (ObjectUtils.isEmpty(roleCode)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        //check role tồn tại trong db
        for (Roles role : roles) {
            if (Roles.of(roleCode).equals(role)) {
                return;
            }
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
}