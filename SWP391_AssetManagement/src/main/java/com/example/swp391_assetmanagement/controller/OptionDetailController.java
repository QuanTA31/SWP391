package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.entity.OptionDetail;
import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.service.OptionDetailService;
import com.example.swp391_assetmanagement.service.servicerequest.OptionDetailListRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.BindingResult;
import java.time.LocalDate;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/option-detail")
public class OptionDetailController {

    private static final int PAGE_SIZE = 10;

    private final OptionDetailService service;

    // mở màn tạo option
    @GetMapping("/create")
    public String createForm(
            @RequestParam("asset_external_request_detail_id") Long assetExternalRequestDetailId,
            HttpSession session,
            Model model) {

        requireAllowedRole(session);
        OptionDetailListWrapper wrapper = new OptionDetailListWrapper();
        List<OptionDetail> plans = new ArrayList<>();
        plans.add(new OptionDetail());

        wrapper.setPlans(plans);

        model.addAttribute("asset_external_request_detail_id", assetExternalRequestDetailId);
        model.addAttribute("plans", wrapper); // ⭐ wrapper, không phải list
        return "optiondetail/create";
    }


    // submit nhiều option
    @PostMapping("/create")
    public String submit(
            @RequestParam("asset_external_request_detail_id") Long assetExternalRequestDetailId,
            @Valid @ModelAttribute("plans") OptionDetailListWrapper wrapper,
            BindingResult bindingResult,
            HttpSession session,
            Model model) {

        requireAllowedRole(session);
        if (bindingResult.hasErrors()) {
            model.addAttribute("asset_external_request_detail_id", assetExternalRequestDetailId);
            return "optiondetail/create";
        }

        for (OptionDetail p : wrapper.getPlans()) {
            p.assetExternalRequestDetailId = assetExternalRequestDetailId;
            p.isSelected = false;
            p.approvedDate = null;
            p.approverBy = null;
        }

        service.saveAll(wrapper.getPlans());
        return "redirect:/option-detail/list?asset_external_request_detail_id=" + assetExternalRequestDetailId;
    }

    // list option
    @GetMapping("/list")
    public String list(
            @RequestParam("asset_external_request_detail_id") Long assetExternalRequestDetailId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", required = false) Integer page,
            HttpSession session,
            Model model) {

        String roleValue = requireAllowedRole(session);
        OptionDetailListWrapper createPlans = new OptionDetailListWrapper();
        OptionDetailListWrapper editPlans = new OptionDetailListWrapper();
        List<OptionDetail> formPlans = new ArrayList<>();
        formPlans.add(new OptionDetail());
        createPlans.setPlans(formPlans);
        List<OptionDetail> editFormPlans = new ArrayList<>();
        editFormPlans.add(new OptionDetail());
        editPlans.setPlans(editFormPlans);
        String selectedStatus = (status == null || status.isBlank()) ? "all" : status;
        int pageIndex = (page == null || page < 1) ? 1 : page;
        Boolean isSelected = parseSelectedStatus(selectedStatus);

        OptionDetailListRequest request = OptionDetailListRequest.builder()
                .requestDetailId(assetExternalRequestDetailId)
                .isSelected(isSelected)
                .offset((pageIndex - 1) * PAGE_SIZE)
                .pageSize(PAGE_SIZE)
                .build();

        List<OptionDetail> plans = service.getByRequestDetailId(
                request.getRequestDetailId(),
                request.getIsSelected(),
                request.getOffset(),
                request.getPageSize()
        );

        int totalItems = service.countByRequestDetailId(
                request.getRequestDetailId(),
                request.getIsSelected()
        );

        int totalPages = Math.max(1, (int) Math.ceil((double) totalItems / PAGE_SIZE));

        model.addAttribute("asset_external_request_detail_id", assetExternalRequestDetailId);
        model.addAttribute("plans", plans);
        model.addAttribute("canApprove", Roles.MANAGER.getValue().equals(roleValue));
        model.addAttribute("canManage", true);
        model.addAttribute("createPlans", createPlans);
        model.addAttribute("editPlans", editPlans);
        model.addAttribute("status", selectedStatus);
        model.addAttribute("page", pageIndex);
        model.addAttribute("pageSize", PAGE_SIZE);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("hasPreviousPage", pageIndex > 1);
        model.addAttribute("hasNextPage", pageIndex < totalPages);
        return "optiondetail/list";
    }

    @GetMapping("/edit/{id}")
    public String editForm(
            @PathVariable("id") Long id,
            @RequestParam(value = "asset_external_request_detail_id", required = false) Long assetExternalRequestDetailId,
            HttpSession session,
            Model model) {

        requireAllowedRole(session);
        OptionDetail plan = service.getById(id).orElse(null);
        if (plan == null) {
            Long redirectRequestDetailId = assetExternalRequestDetailId != null ? assetExternalRequestDetailId : 0L;
            return "redirect:/option-detail/list?asset_external_request_detail_id=" + redirectRequestDetailId;
        }

        Long resolvedRequestDetailId = assetExternalRequestDetailId != null ? assetExternalRequestDetailId : plan.assetExternalRequestDetailId;
        OptionDetailListWrapper wrapper = new OptionDetailListWrapper();
        List<OptionDetail> plans = new ArrayList<>();
        plans.add(plan);
        wrapper.setPlans(plans);

        model.addAttribute("asset_external_request_detail_id", resolvedRequestDetailId);
        model.addAttribute("plans", wrapper);
        return "optiondetail/edit";
    }

    @PostMapping("/edit/{id}")
    public String editSubmit(
            @PathVariable("id") Long id,
            @RequestParam("asset_external_request_detail_id") Long assetExternalRequestDetailId,
            @Valid @ModelAttribute("plans") OptionDetailListWrapper wrapper,
            BindingResult bindingResult,
            HttpSession session,
            Model model) {

        requireAllowedRole(session);
        if (bindingResult.hasErrors()) {
            model.addAttribute("asset_external_request_detail_id", assetExternalRequestDetailId);
            return "optiondetail/edit";
        }

        for (OptionDetail row : wrapper.getPlans()) {
            if (row.id != null) {
                OptionDetail plan = service.getById(row.id).orElse(null);
                if (plan == null) {
                    continue;
                }
                plan.merchant = row.merchant;
                plan.description = row.description;
                plan.unitPrice = row.unitPrice;
                plan.warrantyPeriod = row.warrantyPeriod;
                service.update(plan);
            } else {
                row.assetExternalRequestDetailId = assetExternalRequestDetailId;
                row.isSelected = false;
                row.approvedDate = null;
                row.approverBy = null;
                service.create(row);
            }
        }

        return "redirect:/option-detail/list?asset_external_request_detail_id=" + assetExternalRequestDetailId;
    }

    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable("id") Long id,
            @RequestParam("asset_external_request_detail_id") Long assetExternalRequestDetailId,
            HttpSession session) {

        requireAllowedRole(session);
        service.deleteById(id);
        return "redirect:/option-detail/list?asset_external_request_detail_id=" + assetExternalRequestDetailId;
    }

    @PostMapping("/approval")
    public String updateApproval(
            @RequestParam("id") Long id,
            @RequestParam("asset_external_request_detail_id") Long assetExternalRequestDetailId,
            @RequestParam(value = "selected", required = false) String selected,
            HttpSession session) {

        requireManagerRole(session);
        OptionDetail plan = service.getById(id).orElse(null);
        if (plan == null) {
            return "redirect:/option-detail/list?asset_external_request_detail_id=" + assetExternalRequestDetailId;
        }

        boolean isSelected = selected != null;
        if (isSelected) {
            service.unselectByRequestDetailId(assetExternalRequestDetailId);
        }
        plan.isSelected = isSelected;
        plan.approvedDate = isSelected ? LocalDate.now() : null;
        if (isSelected) {
            Object userId = session.getAttribute("USER_ID");
            if (userId == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Approver is required.");
            }
            plan.approverBy = (Long) userId;
        } else {
            plan.approverBy = null;
        }
        service.update(plan);

        return "redirect:/option-detail/list?asset_external_request_detail_id=" + assetExternalRequestDetailId;
    }

    private String requireAllowedRole(HttpSession session) {
        Object role = session.getAttribute("ROLE");
        if (role == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Role is required.");
        }
        String roleValue = role.toString();
        boolean allowed = Roles.MANAGER.getValue().equals(roleValue)
                || Roles.PURCHASING.getValue().equals(roleValue);
        if (!allowed) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this page.");
        }
        return roleValue;
    }

    private void requireManagerRole(HttpSession session) {
        String roleValue = requireAllowedRole(session);
        if (!Roles.MANAGER.getValue().equals(roleValue)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Manager role is required to approve.");
        }
    }

    private Boolean parseSelectedStatus(String status) {
        if (status == null || "all".equalsIgnoreCase(status)) {
            return null;
        }
        if ("selected".equalsIgnoreCase(status)) {
            return true;
        }
        if ("unselected".equalsIgnoreCase(status)) {
            return false;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status is invalid.");
    }
}




