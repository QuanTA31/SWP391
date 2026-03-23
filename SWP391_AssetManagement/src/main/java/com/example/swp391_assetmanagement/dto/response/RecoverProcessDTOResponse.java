package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
@Builder
public class RecoverProcessDTOResponse {

    private Long requestId;

    private String requestStatusId;

    private String requestStatusName;

    private String requestedBy; // Tên người yêu cầu thay vì ID

    private String requestedDate;

    private String note;

    private List<RecoverItemDetailDTO> items;

    @Builder
    @Getter
    public static class RecoverItemDetailDTO {
        private Long detailId;

        private Long assetId;

        private String assetCode;

        private String assetTypeName;

        private String fromUserName;

        private String fromLocationName;

        private Boolean isDone;
    }
}
