package com.example.swp391_assetmanagement.dto.response;

import lombok.*;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Builder

public class ViewProfileDTOResponse {
    private Long userId;
    private String name;
    private String phone;
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private String locationId;
    private String username;
}
