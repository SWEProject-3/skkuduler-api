package com.skku.skkuduler.dto.response;

import com.skku.skkuduler.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    private Long userId;
    private String email;
    private String name;
    private User.Role role;
}
