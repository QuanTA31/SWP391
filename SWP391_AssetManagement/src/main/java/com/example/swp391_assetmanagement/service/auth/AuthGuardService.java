package com.example.swp391_assetmanagement.service.auth;

import com.example.swp391_assetmanagement.enums.Roles;

public interface AuthGuardService {

    void checkAuthenticated();

    void checkCanAccessRequest(Long requestId);

    Long getCurrentUserId();
    //String getCurrentRole();

    //void checkRole(Roles role);

    // ===== helper methods =====
    void checkManager();

    void checkManagerOrPurchasing();

    boolean canApprove();
}
