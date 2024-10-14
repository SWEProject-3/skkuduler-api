package com.skku.skkuduler.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeDto {
    @NotBlank(message = "Please enter your old password.")
    private String oldPassword;

    @NotBlank(message = "Please enter your new password.")
    @Pattern(
            regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?!.*\\s).{8,16}",
            message = "Password must be 8-16 characters long, and include both letters and numbers."
    )
    private String newPassword;

    @AssertTrue(message = "New password must be different from the old password.")
    public boolean isNewPasswordDifferent() {
        return !newPassword.equals(oldPassword);
    }

}
