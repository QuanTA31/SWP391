package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.enums.AssetStatus;
import com.example.swp391_assetmanagement.service.AssetInternalRequestDetailService;
import com.example.swp391_assetmanagement.service.UserService;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetForRepairServiceResponse;
import com.example.swp391_assetmanagement.service.serviceresponse.LocationViewAssetServiceResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAssetsForRepairUsecase {

    private final UserService userService;
    private final AssetInternalRequestDetailService assetInternalRequestDetailService;

    @Transactional(readOnly = true)
    public List<AssetForRepairServiceResponse> execute(HttpSession session) {

        String userCode = session.getAttribute("USER_CODE").toString();

        LocationViewAssetServiceResponse locationResponse = userService.getLocationViewAsset(userCode);

        if (locationResponse == null || locationResponse.locationId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Không tìm thấy phòng ban của người dùng!");
        }

        return assetInternalRequestDetailService.findAssetsByLocationId(locationResponse.locationId);
    }
}
