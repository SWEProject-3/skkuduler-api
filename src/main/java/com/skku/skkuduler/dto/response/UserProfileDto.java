package com.skku.skkuduler.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    private Long userId;
    private String name;
    private String email;
    private Boolean isMyProfile;
    private Boolean isFriend;
}
