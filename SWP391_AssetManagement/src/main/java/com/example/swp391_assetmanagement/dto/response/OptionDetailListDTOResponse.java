package com.example.swp391_assetmanagement.dto.response;

import com.example.swp391_assetmanagement.service.serviceresponse.OptionDetailServiceResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class OptionDetailListDTOResponse {

    private Long requestDetailId;
    private Long assetRequestId;
    private List<OptionDetailServiceResponse> plans;

    private String status;
    private int page;
    private int pageSize;
    private int totalItems;
    private int totalPages;

    private boolean hasPreviousPage;
    private boolean hasNextPage;
    private boolean canApprove;
    private boolean canManage;
    private boolean hasAnySelected;

    public Map<String, Object> toModel() {
        Map<String, Object> model = new HashMap<>();
        model.put("asset_external_request_detail_id", requestDetailId);
        model.put("assetRequestId", assetRequestId);
        model.put("plans", plans);
        model.put("status", status);
        model.put("page", page);
        model.put("pageSize", pageSize);
        model.put("totalItems", totalItems);
        model.put("totalPages", totalPages);
        model.put("hasPreviousPage", hasPreviousPage);
        model.put("hasNextPage", hasNextPage);
        model.put("canApprove", canApprove);
        model.put("canManage", canManage);
        model.put("hasAnySelected", hasAnySelected);
        return model;
    }
}
