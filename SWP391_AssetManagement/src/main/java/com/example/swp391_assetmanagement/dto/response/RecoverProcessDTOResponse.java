package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
@Builder
public class RecoverProcessDTOResponse {

    private Long requestId;

    private List<RecoverItemDetailDTO> items;

    @Builder
    public static class RecoverItemDetailDTO {

        private Long detailId;

        private String assetCode;

        private Boolean isDone;
    }
}
