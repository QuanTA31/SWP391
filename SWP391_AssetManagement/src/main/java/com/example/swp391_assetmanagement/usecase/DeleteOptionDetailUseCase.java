package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.service.OptionDetailService;
import com.example.swp391_assetmanagement.service.auth.AuthGuardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteOptionDetailUseCase {

    private final OptionDetailService optionDetailService;
    private final AuthGuardService authGuardService;

    public void execute(Long optionDetailId) {
        authGuardService.checkManagerOrPurchasing();

        optionDetailService.deleteById(optionDetailId);
    }
}

