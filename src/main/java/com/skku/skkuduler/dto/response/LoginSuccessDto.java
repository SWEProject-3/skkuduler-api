package com.skku.skkuduler.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginSuccessDto {
    private String accessToken;
    private Long userId;
    private String role;
}
