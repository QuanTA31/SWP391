package com.example.swp391_assetmanagement.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OptionDetailCreateRequest {

    private Long requestDetailId;

    private List<OptionDetailItemRequest> plans;
}

