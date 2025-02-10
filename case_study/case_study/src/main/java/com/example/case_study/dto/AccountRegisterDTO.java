package com.example.case_study.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountRegisterDTO {

    @NotBlank(message = "Username must not be empty")
    @Size(min = 5, max = 50, message = "Username must be between 5 and 50 characters")
    private String username;

    private String password;

    @NotBlank(message = "Full name must not be empty")
    @Size(min = 5, max = 50, message = "Full name must be between 5 and 50 characters")
    private String fullName;

    @Size(max = 15, message = "Phone number must not exceed 15 characters")
    private String phone;

    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;
}
