package com.skku.skkuduler.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateDto {
    @NotBlank(message = "Please enter your name.")
    @Pattern(regexp = "^[a-zA-Z\\s]+$|^[가-힣]+$", message = "Name must contain only Korean characters or English letters with spaces.")
    private String name;
}
