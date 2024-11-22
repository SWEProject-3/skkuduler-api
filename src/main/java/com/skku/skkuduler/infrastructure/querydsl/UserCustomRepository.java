package com.skku.skkuduler.infrastructure.querydsl;

import com.skku.skkuduler.dto.response.DepartmentPermissionDto;
import org.springframework.data.repository.query.Param;

public interface UserCustomRepository {
    DepartmentPermissionDto getDepartmentPermissionDto(@Param("userId") Long userId);
}
