package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.enums.AssetStatus;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MoveAssetRequestToCompletedUsecase {

    private final AssetRequestService assetRequestService;

    @Transactional
    public void execute(Long requestId, HttpSession session) {

        int updated = assetRequestService.moveCompleted(requestId);
        // neu khong update duoc (sai status RESEARCH_DONE)
        if (updated == 0) {
            session.setAttribute("error",
                    "Only RESEARCH_DONE can move to COMPLETED");
            return;
        }
        // lay: tat ca asset thuoc request
        // update asset status -> DISPOSED
        assetRequestService.updateAssetsToDisposed(requestId, AssetStatus.DISPOSED);

        session.setAttribute("success",
                "Moved to COMPLETED successfully");
    }
}
