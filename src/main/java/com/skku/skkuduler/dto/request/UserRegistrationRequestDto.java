package com.skku.skkuduler.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequestDto {

    @NotBlank(message = "Please enter your email.")
    @Email(message = "Email is invalid.")
    private String email;

    @NotBlank(message = "Please enter your password.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "Password must be 8-16 characters long and include uppercase, lowercase letters, numbers, and special characters.")
    private String password;

    @NotBlank(message = "Please enter your name.")
    @Pattern(regexp = "^[가-힣a-zA-Z]+$", message = "Name must consist of 2-10 characters and contain only Korean or English letters.")
    private String name;
}
