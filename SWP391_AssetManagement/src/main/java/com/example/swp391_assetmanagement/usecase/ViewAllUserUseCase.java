package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.ViewAllUserDTORequest;
import com.example.swp391_assetmanagement.dto.response.*;
import com.example.swp391_assetmanagement.enums.AssetStatus;
import com.example.swp391_assetmanagement.enums.Location;
import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.service.UserService;
import com.example.swp391_assetmanagement.service.ViewAllUserService;
import com.example.swp391_assetmanagement.service.servicerequest.ViewAllUserRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.ViewAllUserResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ViewAllUserUseCase {

    private final Integer PAGE_SIZE = 15;
    private final ViewAllUserService viewAllUserService;

    @Transactional(readOnly = true)
    public ViewAllUserDTOResponse viewUser(ViewAllUserDTORequest request, HttpSession session) {
        validateUserRequest( request, session);

        int pageIndex = (request.getPageIndex() != null && request.getPageIndex() != 0) ? request.getPageIndex() : 1;
        // Get data from database
        List<ViewAllUserResponse> serviceResponses = viewAllUserService.selectAllUser(
                ViewAllUserRequest.builder()
                        .locationId(request.getLocationId())
                        .roleID(request.getRoleID())
                        .name(ObjectUtils.isEmpty(request.getName()) ? null : request.getName().trim())
                        .offset((pageIndex - 1) * PAGE_SIZE)
                        .pageSize(PAGE_SIZE)
                        .build());

        if (serviceResponses.isEmpty()) {
            return ViewAllUserDTOResponse.builder()
                    .userDTOResponses(Collections.emptyList())
                    .filters(FilterUserDTOResponse.builder()
                            .locationId(request.getLocationId())
                            .roleId(request.getRoleID())
                            .name(request.getName())
                            .page(pageIndex)
                            .pageSize(PAGE_SIZE)
                            .totalUser(0)
                            .totalPages(1)
                            .hasNextPage(false)
                            .hasPreviousPage(false)
                            .build())
                    .build();
        }
        int totalUsers = serviceResponses.stream().findFirst().get().getTotalUser();

        int totalPages = (int) Math.ceil((double) totalUsers / PAGE_SIZE);
        boolean hasNext = pageIndex < totalPages;
        boolean hasPrevious = pageIndex > 1;

        return ViewAllUserDTOResponse.builder()
                .userDTOResponses(
                        serviceResponses.stream().map(
                                entity -> UserDTOResponse.builder()
                                        .userCode(entity.userCode)
                                        .userStatus(entity.userStatus)
                                        .name(entity.name)
                                        .phone(entity.phone)
                                        .email(entity.email)
                                        .locationName(Location.of(entity.locationId).getName())
                                        .username(entity.username)
                                        .password(entity.password)
                                        .roleName(Roles.of(entity.roleId).getName())
                                        .createAt(entity.getCreateAt())
                                        .build())
                                .toList()
                )
                .filters(FilterUserDTOResponse.builder()
                        .locationId(request.getLocationId())
                        .roleId(request.getRoleID())
                        .name(request.getName())
                        .page(pageIndex)
                        .pageSize(PAGE_SIZE)
                        .totalUser(totalUsers)
                        .totalPages(totalPages)
                        .hasNextPage(hasNext)
                        .hasPreviousPage(hasPrevious)
                        .build())
                .build();
    }

    private void validateUserRequest(ViewAllUserDTORequest request, HttpSession session) {

        // Check role
        if (Objects.equals(session.getAttribute("ROLE"), Roles.PURCHASING.getValue())
                || Objects.equals(session.getAttribute("ROLE"), Roles.DEPARTMENT_MANAGER.getValue())
                || Objects.equals(session.getAttribute("ROLE"), Roles.CLIENT.getValue())
                || Objects.equals(session.getAttribute("ROLE"), Roles.WAREHOUSE.getValue())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào trang này !");
        }

        //Check enums
        if (!ObjectUtils.isEmpty(request.getLocationId()) && !Location.hasValue(request.getLocationId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location is invalid !");
        }
//        if (!ObjectUtils.isEmpty(request.getName()) && !AssetType.hasValue(request.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Asset Type is invalid !");
//        }
        if (!ObjectUtils.isEmpty(request.getRoleID()) && !AssetStatus.hasValue(request.getRoleID())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role is invalid !");
        }
    }
}
