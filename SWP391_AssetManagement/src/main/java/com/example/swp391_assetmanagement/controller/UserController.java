package com.example.swp391_assetmanagement.controller;
import com.example.swp391_assetmanagement.common.RoleChecker;
import com.example.swp391_assetmanagement.dto.request.CreateUserDTORequest;
import com.example.swp391_assetmanagement.dto.request.ViewAllUserDTORequest;
import com.example.swp391_assetmanagement.dto.response.ViewAllUserDTOResponse;
import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.usecase.CreateUserUsecase;
import com.example.swp391_assetmanagement.usecase.UpdateUserStatusUsecase;
import com.example.swp391_assetmanagement.usecase.ViewAllUserUsecase;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserController {
    private final ViewAllUserUsecase viewAllUserUsecase;
    private final CreateUserUsecase createNewUser;
    private final UpdateUserStatusUsecase updateStatusUsecase;
    private final RoleChecker roleChecker;

    @GetMapping("/viewUser")
    public String viewUser(@ModelAttribute ViewAllUserDTORequest request, HttpSession session, Model model) {

        if (session.getAttribute("USER_CODE") == null) {
            return "redirect:/login";
        }

        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.ADMIN);

        ViewAllUserDTOResponse response = viewAllUserUsecase.viewUser(request, session);
        model.addAttribute("users", response);
        model.addAttribute("activePage", "viewUser");
        return "ViewAllUser";
    }
    @PostMapping("/viewUser/updateStatus")
    @ResponseBody
    public ResponseEntity<?> updateStatus(@RequestParam("username") String username,
                                          @RequestParam("status") String status,
                                          HttpSession session) {

        Object userCode = session.getAttribute("USER_CODE");
        if (userCode == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session expired. Please login again.");
        }

        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.ADMIN);

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
    public String showCreateForm(Model model, HttpSession session) {

        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.ADMIN);

        model.addAttribute("userRequest", CreateUserDTORequest.builder().build());
        model.addAttribute("activePage", "createUser");
        return "CreateNewUser";
    }

    @PostMapping("/createUser")
    public String processCreate(@Valid @ModelAttribute("userRequest") CreateUserDTORequest request, BindingResult result, HttpSession session, Model model) {

        if (session.getAttribute("USER_CODE") == null) {
            return "redirect:/login";
        }

        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.ADMIN);

        if (result.hasErrors()) {
            return "CreateNewUser";
        }

        if (!request.isAdult()) {
            model.addAttribute("ageError", "User have 18 years old");
            return "CreateNewUser";
        }

        try {
            createNewUser.createUser(request);
        } catch (ResponseStatusException e) {
            model.addAttribute("errorMessage", e.getReason());
            return "CreateNewUser";
        }
        model.addAttribute("activePage", "viewUser");

        return "redirect:/admin/viewUser";
    }
}
