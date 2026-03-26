package com.example.swp391_assetmanagement.dto.request;

import lombok.*;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Builder

public class UpdateProfileDTORequest {
    private String name;
    private String phone;
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

}
