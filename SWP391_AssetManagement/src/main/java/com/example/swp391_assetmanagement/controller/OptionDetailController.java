package com.example.swp391_assetmanagement.controller;
import com.example.swp391_assetmanagement.dto.request.OptionDetailFormDTORequest;
import com.example.swp391_assetmanagement.usecase.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
@RequiredArgsConstructor
@RequestMapping("/option-detail")
public class OptionDetailController {

    private final CreateOptionDetailUsecase createUseCase;
    private final EditOptionDetailUsecase editUseCase;
    private final ApproveOptionDetailUsecase approveUseCase;
    private final DeleteOptionDetailUsecase deleteUseCase;
    private final GetOptionDetailListUsecase getOptionDetailListUseCase;

    // ================= CREATE =================
    @PostMapping("/create")
    public String create(@RequestParam("asset_external_request_detail_id") Long requestDetailId,
                         @ModelAttribute("createForm") OptionDetailFormDTORequest form,
                         HttpSession session,
                         Model model) {
        try {
            createUseCase.execute(requestDetailId, form, session);
            model.addAttribute("createForm", new OptionDetailFormDTORequest());
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("createForm", form);
        }
        getOptionDetailListUseCase.loadToModel(requestDetailId, null, null, session, model);
        model.addAttribute("editForm", new OptionDetailFormDTORequest());
        return "optiondetail/list";
    }

    // ================= LIST =================
    @GetMapping("/list")
    public String list(@RequestParam("asset_external_request_detail_id") Long requestDetailId,
                       @RequestParam(value = "status", required = false) String status,
                       @RequestParam(value = "page", required = false) Integer page,
                       HttpSession session, Model model) {
        model.addAllAttributes(getOptionDetailListUseCase.execute(requestDetailId, status, page, session).toModel());
        model.addAttribute("createForm", model.containsAttribute("createForm") ? model.getAttribute("createForm") : new OptionDetailFormDTORequest());
        model.addAttribute("editForm", model.containsAttribute("editForm") ? model.getAttribute("editForm") : new OptionDetailFormDTORequest());
        return "optiondetail/list";
    }

    // ================= EDIT =================
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @RequestParam("asset_external_request_detail_id") Long requestDetailId,
                       @ModelAttribute("editForm") OptionDetailFormDTORequest form,
                       HttpSession session,
                       Model model) {
        try {
            editUseCase.execute(id,requestDetailId, form, session);
            model.addAttribute("editForm", new OptionDetailFormDTORequest());
        } catch (IllegalArgumentException ex) {
            model.addAttribute("editErrorMessage", ex.getMessage());
            model.addAttribute("editForm", form);
            model.addAttribute("openEditModal", true);
        }
        getOptionDetailListUseCase.loadToModel(requestDetailId, null, null, session, model);
        model.addAttribute("createForm", new OptionDetailFormDTORequest());
        return "optiondetail/list";
    }

    // ================= DELETE =================
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, @RequestParam("asset_external_request_detail_id") Long requestDetailId,
                         HttpSession session) {
        deleteUseCase.execute(id, session);
        return "redirect:/option-detail/list?asset_external_request_detail_id=" + requestDetailId;
    }

    // ================= APPROVAL =================
    @PostMapping("/approval")
    public String approve(@RequestParam("id") Long optionId,
                          @RequestParam("asset_external_request_detail_id") Long requestDetailId,
                          HttpSession session
                          //@RequestParam(value = "selected", required = false) String selected
                          ) {
        approveUseCase.execute(optionId, requestDetailId,true, session);
        return "redirect:/option-detail/list?asset_external_request_detail_id=" + requestDetailId;
    }

    /*@PostMapping("/submit")
    public String submit(
            @RequestParam("assetExternalRequestDetailId") Long detailId
    ) {
        submitExternalRequestUsecase.execute(detailId);
        return "redirect:/option-detail/list?asset_external_request_detail_id=" + detailId;
    }*/
}
