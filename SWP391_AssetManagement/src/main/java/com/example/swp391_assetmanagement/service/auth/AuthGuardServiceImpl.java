package com.example.swp391_assetmanagement.service.auth;

import com.example.swp391_assetmanagement.enums.Roles;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthGuardServiceImpl implements AuthGuardService {

    private final HttpSession session;

    @Override
    public void checkAuthenticated() {
        Object userId = session.getAttribute("USER_ID");
        if (userId == null) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "User is not authenticated"
            );
        }
    }

    @Override
    public void checkCanAccessRequest(Long requestId) {
        checkAuthenticated();

        // 👉 TẠM THỜI: cho phép toàn bộ
        // Sau này: check requestId có thuộc user / department không
        boolean allowed = true;

        if (!allowed) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Cannot access this request"
            );
        }
    }

    // ====== HÀM TIỆN DÙNG (KHUYẾN NGHỊ) ======

    public void checkManager() {
        checkRole(Roles.MANAGER); // "02"
    }

    public void checkManagerOrPurchasing() {
        checkAuthenticated();
        String role = session.getAttribute("ROLE").toString();

        if (!Roles.MANAGER.getValue().equals(role)
                && !Roles.PURCHASING.getValue().equals(role)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Permission denied"
            );
        }
    }

    @Override
    public Long getCurrentUserId() {
        Object userId = session.getAttribute("USER_ID");
        if (userId == null) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "User is not authenticated"
            );
        }
        return (Long) userId;
    }
/*
    @Override
    private String getCurrentRole() {
        Object role = session.getAttribute("ROLE");
        if (role == null) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Role is required"
            );
        }
        return role.toString();
    }
*/
    // ===== PRIVATE HELPERS =====

    @Override
    public boolean canApprove() {
        Roles role = getCurrentRole();
        return role == Roles.MANAGER;
    }

    // ===== PRIVATE HELPER =====
    private Roles getCurrentRole() {
        Object roleObj = session.getAttribute("ROLE");
        if (roleObj == null) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Role is required"
            );
        }

        return Roles.of(roleObj.toString()); // "02" -> MANAGER
    }

    public void checkRole(Roles requiredRole) {
        checkAuthenticated();

        Object roleObj = session.getAttribute("ROLE");
        if (roleObj == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Role is required");
        }

        String currentRoleValue = roleObj.toString(); // "02"

        if (!requiredRole.getValue().equals(currentRoleValue)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Permission denied"
            );
        }
    }

    private ResponseStatusException forbidden() {
        return new ResponseStatusException(HttpStatus.FORBIDDEN, "Permission denied");
    }

}
