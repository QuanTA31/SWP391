package com.example.swp391_assetmanagement.controller;
import com.example.swp391_assetmanagement.dto.request.CreateUserDTORequest;
import com.example.swp391_assetmanagement.dto.request.ViewAllUserDTORequest;
import com.example.swp391_assetmanagement.dto.response.ViewAllUserDTOResponse;
import com.example.swp391_assetmanagement.usecase.CreateUserUsecase;
import com.example.swp391_assetmanagement.usecase.UpdateUserStatusUsecase;
import com.example.swp391_assetmanagement.usecase.ViewAllUserUsecase;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserController {
    private final ViewAllUserUsecase viewAllUserUsecase;
    private final CreateUserUsecase createNewUser;
    private final UpdateUserStatusUsecase updateStatusUsecase;

    @GetMapping("/viewUser")
    public String viewUser(@ModelAttribute ViewAllUserDTORequest request, HttpSession session, Model model) {

        ViewAllUserDTOResponse response = viewAllUserUsecase.viewUser(request, session);
        model.addAttribute("users", response);

        return "ViewAllUser";
    }
    @PostMapping("/viewUser/updateStatus")
    @ResponseBody
    public ResponseEntity<?> updateStatus(@RequestParam("username") String username,
                                          @RequestParam("status") String status,
                                          HttpSession session) {
        try {
            // Gọi Usecase xử lý
            updateStatusUsecase.execute(username, status, session);

            return ResponseEntity.ok("Update success!");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error");
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
