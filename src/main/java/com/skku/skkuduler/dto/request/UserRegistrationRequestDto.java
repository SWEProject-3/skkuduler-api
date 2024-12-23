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
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?!.*\\s).{8,16}", message = "Password must be 8-16 characters long, and include both letters and numbers.")
    private String password;

    @NotBlank(message = "Please enter your name.")
    @Pattern(regexp = "^[a-zA-Z\\s]+$|^[가-힣]+$", message = "Name must contain only Korean characters or English letters with spaces.")
    private String name;
}
