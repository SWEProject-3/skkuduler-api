package com.skku.skkuduler.infrastructure;

import com.skku.skkuduler.domain.calender.Event;
import com.skku.skkuduler.dto.response.CalendarEventSummaryDto;
import com.skku.skkuduler.infrastructure.querydsl.EventCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long>, EventCustomRepository {

    @Query("""
    SELECT e
    FROM event e
    LEFT JOIN FETCH e.image
    WHERE e.departmentId IS NULL AND e.userId IS NULL AND e.isUserEvent = false
    """)
    List<Event> findCommonDepartmentEvents();
}
