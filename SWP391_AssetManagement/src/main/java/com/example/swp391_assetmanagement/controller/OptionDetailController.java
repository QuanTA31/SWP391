package com.example.swp391_assetmanagement.controller;
import com.example.swp391_assetmanagement.dto.request.OptionDetailFormRequest;
import com.example.swp391_assetmanagement.usecase.*;
import com.example.swp391_assetmanagement.dto.response.OptionDetailListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
@RequiredArgsConstructor
@RequestMapping("/option-detail")
public class OptionDetailController {
    private final CreateOptionDetailUseCase createUseCase;
    private final EditOptionDetailUseCase editUseCase;
    private final ApproveOptionDetailUseCase approveUseCase;
    private final DeleteOptionDetailUseCase deleteUseCase;
    private final GetOptionDetailListUseCase getOptionDetailListUseCase;

    @PostMapping("/create")
    public String create(
            @RequestParam("asset_external_request_detail_id") Long requestDetailId,
            @ModelAttribute("createForm") OptionDetailFormRequest form,
            RedirectAttributes redirectAttributes
    ) {
        try {
            createUseCase.execute(requestDetailId, form);
            return "redirect:/option-detail/list?asset_external_request_detail_id=" + requestDetailId;
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            redirectAttributes.addFlashAttribute("createForm", form);
            return "redirect:/option-detail/list?asset_external_request_detail_id=" + requestDetailId;
        }
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
        if (!model.containsAttribute("createForm")) {
            model.addAttribute("createForm", new OptionDetailFormRequest());
        }
        if (!model.containsAttribute("editForm")) {
            model.addAttribute("editForm", new OptionDetailFormRequest());
        }
        return "optiondetail/list";
    }

    // ================= EDIT =================
    @PostMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            @RequestParam("asset_external_request_detail_id") Long requestDetailId,
            @ModelAttribute("editForm") OptionDetailFormRequest form,
            RedirectAttributes redirectAttributes
    ) {
        try {
            form.setId(id);
            editUseCase.execute(requestDetailId, form);
            return "redirect:/option-detail/list?asset_external_request_detail_id=" + requestDetailId;
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("editErrorMessage", ex.getMessage());
            redirectAttributes.addFlashAttribute("editForm", form);
            redirectAttributes.addFlashAttribute("openEditModal", true);
            return "redirect:/option-detail/list?asset_external_request_detail_id=" + requestDetailId;
        }
    }

    // ================= DELETE =================
    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable Long id,
            @RequestParam("asset_external_request_detail_id") Long requestDetailId
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
