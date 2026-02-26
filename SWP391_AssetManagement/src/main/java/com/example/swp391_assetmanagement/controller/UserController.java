package com.example.swp391_assetmanagement.controller;
import com.example.swp391_assetmanagement.dto.request.CreateUserDTORequest;
import com.example.swp391_assetmanagement.dto.request.ViewAllUserDTORequest;
import com.example.swp391_assetmanagement.dto.response.ViewAllUserDTOResponse;
import com.example.swp391_assetmanagement.usecase.CreateUserUsecase;
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
    private final CreateUserUsecase createNewUser;

    @GetMapping("/viewUser")
    public String viewUser(@ModelAttribute ViewAllUserDTORequest request, HttpSession session, Model model) {

        ViewAllUserDTOResponse response = viewAllUserUsecase.viewUser(request, session);
        model.addAttribute("users", response);

        return "ViewAllUser";
    }
    @GetMapping("/createUser")
    public String showCreateForm(Model model) {
        model.addAttribute("userRequest", new CreateUserDTORequest());
        return "CreateNewUser";
    }

    @PostMapping("/createUser")
    public String processCreate(@ModelAttribute("userRequest") CreateUserDTORequest request, HttpSession session) {

        createNewUser.createUser(request, session);

        return "redirect:/admin/viewUser";
    }
}
