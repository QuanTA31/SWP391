package com.example.swp391_assetmanagement.controller;
import com.example.swp391_assetmanagement.dto.request.CreateUserDTORequest;
import com.example.swp391_assetmanagement.dto.request.ViewAllUserDTORequest;
import com.example.swp391_assetmanagement.dto.response.ViewAllUserDTOResponse;
import com.example.swp391_assetmanagement.usecase.ViewAllUserUsecase;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserController {
    private final ViewAllUserUsecase viewAllUserUsecase;

    @GetMapping("/viewUser")
    public String viewUser(@ModelAttribute ViewAllUserDTORequest request, HttpSession session, Model model) {

        ViewAllUserDTOResponse response = viewAllUserUsecase.viewUser(request, session);
        model.addAttribute("users", response);

        return "ViewAllUser";
    }
    @GetMapping("/createUser")
    public String showCreateForm(Model model) {
        // Gửi một object rỗng sang để bind dữ liệu form
        model.addAttribute("userRequest", new CreateUserDTORequest());
        return "ManagerCreateUser";
    }

    @PostMapping("/createUser")
    public String processCreate(@ModelAttribute("userRequest") CreateUserDTORequest request) {
        // Logic tạo User Code tự động: Role + 4 số tăng dần
        // Ví dụ: ADMIN (01) -> A0001
        String prefix = getPrefixByRole(request.getRoleId());
        String nextNumber = "0001"; // Logic này nên lấy từ DB: count(*) + 1
        String autoUserCode = prefix + nextNumber;

        request.setUserCode(autoUserCode);

        // Gọi service lưu vào 2 bảng users và user_detail
        // createUserService.createNewUser(request);

        return "redirect:/main/viewUser";
    }
    private String getPrefixByRole(String roleId) {
        return switch (roleId) {
            case "01" -> "A"; // ADMIN
            case "02" -> "M"; // MANAGER
            case "03" -> "W"; // WAREHOUSE
            case "04" -> "P"; // PURCHASING
            case "05" -> "D"; // DEPARTMENT_MANAGER
            default -> "C";   // CLIENT
        };
    }
}
