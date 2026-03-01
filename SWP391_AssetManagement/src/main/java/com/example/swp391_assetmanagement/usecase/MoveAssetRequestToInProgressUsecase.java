package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class MoveAssetRequestToInProgressUsecase {

    private final AssetRequestService assetRequestService;

    public void execute(Long requestId, HttpSession session) {

        int updated = assetRequestService.moveInProgress(requestId);

        if (updated == 0) {
            session.setAttribute("error",
                    "Only RESEARCH_DONE can move to IN_PROGRESS");
            return;
        }

        session.setAttribute("success",
                "Moved to IN_PROGRESS successfully");
    }
}
