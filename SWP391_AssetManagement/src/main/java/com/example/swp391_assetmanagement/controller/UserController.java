package com.example.swp391_assetmanagement.controller;
import com.example.swp391_assetmanagement.dto.request.CreateUserDTORequest;
import com.example.swp391_assetmanagement.dto.request.ViewAllUserDTORequest;
import com.example.swp391_assetmanagement.dto.response.ViewAllUserDTOResponse;
import com.example.swp391_assetmanagement.usecase.CreateUserUsecase;
import com.example.swp391_assetmanagement.usecase.ViewAllUserUsecase;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserController {
    private final ViewAllUserUsecase viewAllUserUsecase;
    private final CreateUserUsecase createNewUser;

    @GetMapping("/viewUser")
    public String viewUser(@ModelAttribute ViewAllUserDTORequest request, HttpSession session, Model model) {

        ViewAllUserDTOResponse response = viewAllUserUsecase.viewUser(request, session);
        model.addAttribute("users", response);

        return "ViewAllUser";
    }
    @PostMapping("/viewUser/updateStatus") // Đường dẫn đầy đủ sẽ là /admin/viewUser/updateStatus
    @ResponseBody // Phải có cái này để trả về kết quả trực tiếp, không tìm file HTML
    public ResponseEntity<?> updateStatus(@RequestParam("username") String username,
                                          @RequestParam("status") String status) {
        try {
            // In log để kiểm tra xem dữ liệu đã xuống tới đây chưa
            System.out.println("Update Status - User: " + username + ", New Status: " + status);

            // Gọi Usecase xử lý logic DB ở đây
            // updateStatusUsecase.execute(username, status);

            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/createUser")
    public String showCreateForm(Model model) {
        model.addAttribute("userRequest", CreateUserDTORequest.builder().build());
        return "CreateNewUser";
    }

    @PostMapping("/createUser")
    public String processCreate(@ModelAttribute("userRequest") CreateUserDTORequest request, HttpSession session) {

        createNewUser.createUser(request, session);

        return "redirect:/admin/viewUser";
    }
}
