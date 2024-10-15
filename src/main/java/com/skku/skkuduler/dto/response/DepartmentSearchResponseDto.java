package com.skku.skkuduler.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentSearchResponseDto {
    private Long departmentId;
    private String departmentName;
    private boolean isSubscribed;
}
