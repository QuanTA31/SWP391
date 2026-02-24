package com.example.swp391_assetmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/purchase-requests")
@RequiredArgsConstructor
public class PurchaseRequestController {

    @GetMapping("/warehouse/createRequest")
    public String showLoginPage() {
        return "createPurchaseRequest";
    }
}
