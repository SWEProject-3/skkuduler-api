package com.skku.skkuduler.infrastructure;

import com.skku.skkuduler.domain.calender.Calender;
import com.skku.skkuduler.dto.response.CalenderInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CalenderRepository extends JpaRepository<Calender,Long> {
    @Query("""
    SELECT new com.skku.skkuduler.dto.response.CalenderInfoDto(
        c.calenderId,
        c.name
    )
    FROM calender c
    WHERE c.userId = :userId AND c.isGlobal = false
    """)
    List<CalenderInfoDto> findAllUserCalendersToCalenderInfoDto(@Param("userId") Long userId);

    @Query("""
    SELECT new com.skku.skkuduler.dto.response.CalenderInfoDto(
        c.calenderId,
        c.name
    )
    FROM calender c
    WHERE c.departmentId = :departmentId AND c.isGlobal = true
    """)
    List<CalenderInfoDto> findAllDeptCalendersToCalenderInfoDto(@Param("departmentId") Long departmentId);
}
