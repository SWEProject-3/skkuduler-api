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
    @Pattern(regexp = "^[가-힣a-zA-Z]+$", message = "Name must consist of 2-10 characters and contain only Korean or English letters.")
    private String name;
}
