package com.example.swp391_assetmanagement.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Builder
public class CreateUserDTORequest {

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 7, max = 99, message = "Username must be at least 7 characters long")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(max = 99, message = "Invalid password")
    private String password;

    @NotBlank(message = "Name cannot be empty")
    @Size(max = 99, message = "Invalid name")
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Size(max = 99, message = "Invalid email")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits")
    private String phone;

    @NotNull(message = "Date of birth cannot be empty")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Role ID is required")
    private String roleId;

    @NotBlank(message = "Status ID is required")
    private String statusId;

    @NotBlank(message = "Location ID is required")
    private String locationId;

    // Logic: Age >= 18
    public boolean isAdult() {
        if (dateOfBirth == null) return false;
        return Period.between(dateOfBirth, LocalDate.now()).getYears() >= 18;
    }
}