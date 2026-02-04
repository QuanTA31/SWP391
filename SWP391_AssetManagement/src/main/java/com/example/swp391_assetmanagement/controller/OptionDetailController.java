package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.dto.request.OptionDetailCreateRequest;
import com.example.swp391_assetmanagement.dto.request.OptionDetailItemRequest;
import com.example.swp391_assetmanagement.entity.OptionDetail;
import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.service.OptionDetailService;
import com.example.swp391_assetmanagement.dto.request.OptionDetailListRequest;
import com.example.swp391_assetmanagement.usecase.*;
import com.example.swp391_assetmanagement.dto.response.OptionDetailListResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/option-detail")
public class OptionDetailController {

    private static final int PAGE_SIZE = 10;

    private final OptionDetailService optionDetailService;

    private final CreateOptionDetailUseCase createUseCase;
    private final EditOptionDetailUseCase editUseCase;
    private final ApproveOptionDetailUseCase approveUseCase;
    private final DeleteOptionDetailUseCase deleteUseCase;
    private final GetOptionDetailListUseCase getOptionDetailListUseCase;

    private List<OptionDetail> mapToEntities(
            List<OptionDetailItemRequest> items,
            Long requestDetailId
    ) {
        return items.stream().map(item -> {
            OptionDetail option = new OptionDetail();
            option.setAssetExternalRequestDetailId(requestDetailId);
            option.setUnitPrice(item.getUnitPrice());
            option.setDescription(item.getDescription());
            option.setMerchant(item.getMerchant());
            option.setWarrantyPeriod(item.getWarrantyPeriod());
            option.setIsSelected(false);
            return option;
        }).toList();
    }





    // ================= CREATE =================
    @PostMapping("/create")
    public String create(
            @RequestParam("asset_external_request_detail_id") Long requestDetailId,
            @Valid @ModelAttribute("plans") OptionDetailListWrapper wrapper,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("asset_external_request_detail_id", requestDetailId);
            return "optiondetail/list";
        }

        createUseCase.execute(requestDetailId, wrapper.getPlans());

        return "redirect:/option-detail/list?asset_external_request_detail_id=" + requestDetailId;
    }

    @PostMapping("/create/ajax")
    @ResponseBody
    public void createAjax(
            @RequestParam("asset_external_request_detail_id") Long requestDetailId,
            @RequestBody OptionDetailCreateRequest request
    ) {
        List<OptionDetail> entities =
                mapToEntities(request.getPlans(), requestDetailId);

        createUseCase.execute(requestDetailId, entities);
    }






    // ================= LIST =================
    @GetMapping("/list")
    public String list(
            @RequestParam("asset_external_request_detail_id") Long requestDetailId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", required = false) Integer page,
            Model model
    ) {
        OptionDetailListResponse result =
                getOptionDetailListUseCase.execute(requestDetailId, status, page);

        model.addAllAttributes(result.toModel());

        OptionDetailListWrapper createPlans = new OptionDetailListWrapper();
        createPlans.setPlans(List.of(new OptionDetail()));

        OptionDetailListWrapper editPlans = new OptionDetailListWrapper();
        editPlans.setPlans(List.of(new OptionDetail()));

        model.addAttribute("createPlans", createPlans);
        model.addAttribute("editPlans", editPlans);

        return "optiondetail/list";
    }

    // ================= EDIT =================
    @PostMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            @RequestParam("asset_external_request_detail_id") Long requestDetailId,
            @Valid @ModelAttribute("plans") OptionDetailListWrapper wrapper,
            BindingResult bindingResult,
            HttpSession session,
            Model model
    ) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("asset_external_request_detail_id", requestDetailId);
            return "optiondetail/list";
        }

        editUseCase.execute(requestDetailId, wrapper.getPlans());

        return "redirect:/option-detail/list?asset_external_request_detail_id=" + requestDetailId;
    }

    // ================= DELETE =================
    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable Long id,
            @RequestParam("asset_external_request_detail_id") Long requestDetailId,
            HttpSession session
    ) {

        deleteUseCase.execute(id);

        return "redirect:/option-detail/list?asset_external_request_detail_id=" + requestDetailId;
    }

    // ================= APPROVAL =================
    @PostMapping("/approval")
    public String approve(
            @RequestParam("id") Long optionId,
            @RequestParam("asset_external_request_detail_id") Long requestDetailId,
            @RequestParam(value = "selected", required = false) String selected
    ) {
        approveUseCase.execute(
                optionId,
                requestDetailId,
                selected != null
        );

        return "redirect:/option-detail/list?asset_external_request_detail_id=" + requestDetailId;
    }

}
