package com.example.swp391_assetmanagement.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ViewAllUserDTORequest {

    private String locationId;

    private String roleID;
    //user input by keyboard to search user by name.
    private String name;

    //private String userStatus;

    //private String searchWord;

    private Integer pageIndex;

}
