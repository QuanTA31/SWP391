package com.example.swp391_assetmanagement.controller;
import com.example.swp391_assetmanagement.dto.request.OptionDetailFormRequest;
import com.example.swp391_assetmanagement.usecase.*;
import com.example.swp391_assetmanagement.dto.response.OptionDetailListDTOResponse;
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

    @PostMapping("/create")
    public String create(@RequestParam("asset_external_request_detail_id") Long requestDetailId, @ModelAttribute("createForm") OptionDetailFormRequest form, RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            createUseCase.execute(requestDetailId, form, session);
            return "redirect:/option-detail/list?asset_external_request_detail_id=" + requestDetailId;
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            redirectAttributes.addFlashAttribute("createForm", form);
            return "redirect:/option-detail/list?asset_external_request_detail_id=" + requestDetailId;
        }
    }

    // ================= LIST =================
//    @GetMapping("/list")
//    public String list(@RequestParam("asset_external_request_detail_id") Long requestDetailId, @RequestParam(value = "status", required = false) String status, @RequestParam(value = "page", required = false) Integer page, HttpSession session, Model model) {
//        model.addAllAttributes(getOptionDetailListUseCase.execute(requestDetailId, status, page, session).toModel());
//        model.addAttribute("createForm", model.containsAttribute("createForm") ? model.getAttribute("createForm") : new OptionDetailFormRequest());
//        model.addAttribute("editForm", model.containsAttribute("editForm") ? model.getAttribute("editForm") : new OptionDetailFormRequest());
//        return "optiondetail/list";
//    }
    @GetMapping("/list")
    public String list(
            @RequestParam("asset_external_request_detail_id") Long requestDetailId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", required = false) Integer page,
            HttpSession session,
            Model model) {

        // TRUYỀN THÊM 'false' VÀO ĐÂY:
        // Vì đây là màn hình list chung cho cả Purchasing và Manager xem, không phải màn hình Approve chuyên biệt.
        var response = getOptionDetailListUseCase.execute(requestDetailId, status, page, session, false);

        model.addAllAttributes(response.toModel());

        model.addAttribute("createForm", model.containsAttribute("createForm") ? model.getAttribute("createForm") : new OptionDetailFormRequest());
        model.addAttribute("editForm", model.containsAttribute("editForm") ? model.getAttribute("editForm") : new OptionDetailFormRequest());

        return "optiondetail/list";
    }

    // ================= EDIT =================
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @RequestParam("asset_external_request_detail_id") Long requestDetailId, @ModelAttribute("editForm") OptionDetailFormRequest form, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            form.setId(id);
            editUseCase.execute(requestDetailId, form, session);
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
    public String delete(@PathVariable Long id, @RequestParam("asset_external_request_detail_id") Long requestDetailId, HttpSession session) {
        deleteUseCase.execute(id, session);
        return "redirect:/option-detail/list?asset_external_request_detail_id=" + requestDetailId;
    }

    // ================= APPROVAL =================
    @PostMapping("/approval")
    public String approve(@RequestParam("id") Long optionId, @RequestParam("asset_external_request_detail_id") Long requestDetailId, @RequestParam(value = "selected", required = false) String selected, HttpSession session) {
        approveUseCase.execute(optionId, requestDetailId,selected != null, session);
        return "redirect:/option-detail/list?asset_external_request_detail_id=" + requestDetailId;
    }
}
