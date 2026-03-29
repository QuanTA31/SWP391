package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dao.AssetRequestDAO;
import com.example.swp391_assetmanagement.dao.AssetsDAO;
import com.example.swp391_assetmanagement.dto.request.ViewLiquidationAssetDTORequest;
import com.example.swp391_assetmanagement.dto.response.LiquidationAssetDTOResponse;
import com.example.swp391_assetmanagement.dto.response.ViewLiquidationAssetAllDTOResponse;
import com.example.swp391_assetmanagement.enums.RequestType;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ViewLiquidationAssetAllUsecase {

    private final AssetsDAO assetsDAO;
    private final AssetRequestDAO assetRequestDAO;
    private final AssetRequestService assetRequestService;

    @Transactional(readOnly = true)
    public ViewLiquidationAssetAllDTOResponse execute(ViewLiquidationAssetDTORequest request, HttpSession session) {
        Long requestId = request.getAssetRequestId();
        
        // Basic validation
        String requestType = assetRequestService.findRequestTypeById(requestId);
        if (!Objects.equals(RequestType.LIQUIDATION.getValue(), requestType)) {
            throw new IllegalArgumentException("Invalid request type for liquidation view");
        }

        int pageIndex = (request.getPageIndex() == null || request.getPageIndex() < 1) ? 1 : request.getPageIndex();
        int pageSize = 10;
        long offset = (long) (pageIndex - 1) * pageSize;

        //  Lấy danh sách tài sản cụ thể từ database.
        List<LiquidationAssetDTOResponse> assets = assetsDAO.findLiquidationAssetsByRequestId(
                requestId,
                request.getAssetTypeId(),
                request.getSearchWord(),
                offset,
                pageSize
        );

        //  Lấy các con số tổng quát để hiển thị lên giao diện.
        int totalItems = assetsDAO.countLiquidationAssetsByRequestId(
                requestId,
                request.getAssetTypeId(),
                request.getSearchWord()
        );
        BigDecimal totalAmount = assetsDAO.sumLiquidationAmountByRequestId(requestId); //  Tổng số lượng tài sản
        if (totalAmount == null) totalAmount = BigDecimal.ZERO;  // Tổng số tiền dự kiến thu về từ việc thanh lý lô hàng này.

        String requestStatus = assetRequestDAO.getStatusById(requestId);
        
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        request.setTotalItems(totalItems);
        request.setTotalPages(totalPages);
        request.setHasPreviousPage(pageIndex > 1);
        request.setHasNextPage(pageIndex < totalPages);

        // Trả về một "gói dữ liệu" đầy đủ cho View.
        return ViewLiquidationAssetAllDTOResponse.builder()
                .liquidationAssets(assets)
                .filter(request)
                .totalAmount(totalAmount)
                .requestStatus(requestStatus)
                .build();
    }
}
