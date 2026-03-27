package com.example.swp391_assetmanagement.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class InventoryCompleteDTORequest {
    private Long requestId;
    private List<Item> items;

    @Getter
    @Setter
    public static class Item {
        private Long detailId;
        private Boolean isDone;
        private String note;
    }
}
