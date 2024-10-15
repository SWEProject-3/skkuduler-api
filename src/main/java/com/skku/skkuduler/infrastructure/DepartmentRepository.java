package com.skku.skkuduler.infrastructure;

import com.skku.skkuduler.domain.department.Department;
import com.skku.skkuduler.dto.response.DepartmentSearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

public interface DepartmentRepository extends JpaRepository<Department,Long> {
    @Query("""
    SELECT new com.skku.skkuduler.dto.response.DepartmentSearchResponseDto(
        d.departmentId,
        d.name,
        CASE WHEN COUNT(s) > 0 THEN true ELSE false END
    )
    FROM department d
    LEFT JOIN subscription s ON d.departmentId = s.departmentId AND s.user.userId = :userId
    WHERE d.name like %:query%
    GROUP BY d.departmentId, d.name
    """
    )
    Page<DepartmentSearchResponseDto> searchDepartmentsByUserId(@Param("query") String query, Pageable pageable, @Param("userId")Long userId);

    @Query("""
    SELECT new com.skku.skkuduler.dto.response.DepartmentSearchResponseDto(
        d.departmentId,
        d.name,
        CASE WHEN COUNT(s) > 0 THEN true ELSE false END
    )
    FROM department d
    LEFT JOIN subscription s ON d.departmentId = s.departmentId AND s.user.userId = :userId
    GROUP BY d.departmentId, d.name
    """
    )
    Page<DepartmentSearchResponseDto> findAllDepartmentsByUserId(Pageable pageable, @Param("userId") Long userId);
}
