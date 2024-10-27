package com.skku.skkuduler.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalenderInfoDto {
    private Long calenderId;
    private String calenderName;
}
