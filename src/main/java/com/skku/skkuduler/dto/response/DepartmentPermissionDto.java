package com.skku.skkuduler.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentPermissionDto {
    private List<PermissionInfo> permissions;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PermissionInfo{
        private Long departmentId;
        private String departmentName;
    }
}
