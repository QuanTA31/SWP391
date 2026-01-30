package com.example.swp391_assetmanagement.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/departmentManager")
@RequiredArgsConstructor
public class DepartmentManagerController {
    @GetMapping("/newAllocationRequest")
    public String newAllocationRequest(@ModelAttribute HttpSession session, Model model){



        return "NewAllocationRequest";
    }
}
