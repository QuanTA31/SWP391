package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.entity.OptionDetail;
import com.example.swp391_assetmanagement.service.ProcurementPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.BindingResult;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/procurement")
public class ProcurementPlanController {

    private final ProcurementPlanService service;

    // mở màn tạo option
    @GetMapping("/create")
    public String createForm(
            @RequestParam("processId") Long processId,
            Model model) {

        ProcurementPlanListWrapper wrapper = new ProcurementPlanListWrapper();
        List<OptionDetail> plans = new ArrayList<>();
        plans.add(new OptionDetail());

        wrapper.setPlans(plans);

        model.addAttribute("processId", processId);
        model.addAttribute("plans", wrapper); // ⭐ wrapper, không phải list
        return "procurement/create";
    }


    // submit nhiều option
    @PostMapping("/create")
    public String submit(
            @RequestParam("processId") Long processId,
            @Valid @ModelAttribute("plans") ProcurementPlanListWrapper wrapper,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("processId", processId);
            return "procurement/create";
        }

        for (OptionDetail p : wrapper.getPlans()) {
            p.assetExternalProcessId = processId;
            p.approvalStatusId = "01"; // PENDING
        }

        service.saveAll(wrapper.getPlans());
        return "redirect:/procurement/list?processId=" + processId;
    }

    // list option
    @GetMapping("/list")
    public String list(
            @RequestParam("processId") Long processId,
            Model model) {

        model.addAttribute("processId", processId);
        model.addAttribute("plans",
                service.getByProcessId(processId));
        return "procurement/list";
    }

    @GetMapping("/edit/{id}")
    public String editForm(
            @PathVariable("id") Long id,
            @RequestParam(value = "processId", required = false) Long processId,
            Model model) {

        OptionDetail plan = service.getById(id).orElse(null);
        if (plan == null) {
            Long redirectProcessId = processId != null ? processId : 0L;
            return "redirect:/procurement/list?processId=" + redirectProcessId;
        }

        Long resolvedProcessId = processId != null ? processId : plan.assetExternalProcessId;
        ProcurementPlanListWrapper wrapper = new ProcurementPlanListWrapper();
        List<OptionDetail> plans = new ArrayList<>();
        plans.add(plan);
        wrapper.setPlans(plans);

        model.addAttribute("processId", resolvedProcessId);
        model.addAttribute("plans", wrapper);
        return "procurement/edit";
    }

    @PostMapping("/edit/{id}")
    public String editSubmit(
            @PathVariable("id") Long id,
            @RequestParam("processId") Long processId,
            @Valid @ModelAttribute("plans") ProcurementPlanListWrapper wrapper,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("processId", processId);
            return "procurement/edit";
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
                service.update(plan);
            } else {
                row.assetExternalProcessId = processId;
                row.approvalStatusId = "01"; // PENDING
                service.create(row);
            }
        }

        return "redirect:/procurement/list?processId=" + processId;
    }

    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable("id") Long id,
            @RequestParam("processId") Long processId) {

        service.deleteById(id);
        return "redirect:/procurement/list?processId=" + processId;
    }

    @PostMapping("/approval")
    public String updateApproval(
            @RequestParam("id") Long id,
            @RequestParam("processId") Long processId,
            @RequestParam("approvalStatusId") String approvalStatusId) {

        OptionDetail plan = service.getById(id).orElse(null);
        if (plan == null) {
            return "redirect:/procurement/list?processId=" + processId;
        }

        plan.approvalStatusId = approvalStatusId;
        plan.approverBy = 1L;
        plan.approvedAt = LocalDateTime.now();
        service.update(plan);

        return "redirect:/procurement/list?processId=" + processId;
    }

}
