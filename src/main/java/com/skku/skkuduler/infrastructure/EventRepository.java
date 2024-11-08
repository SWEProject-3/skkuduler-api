package com.skku.skkuduler.infrastructure;

import com.skku.skkuduler.domain.calender.Event;
import com.skku.skkuduler.dto.EventDetail;
import com.skku.skkuduler.dto.response.CalendarEventDetailDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {

    @Query("""
    SELECT DISTINCT new com.skku.skkuduler.dto.EventDetail(
    CASE WHEN e.isUserEvent = TRUE THEN FALSE ELSE TRUE END,
    CASE WHEN e.userId = :viewerId THEN TRUE ELSE FALSE END,
    FALSE,
    e.userId,
    owner.name,
    e.departmentId,
    d.name,
    e,
    null
    )
    FROM event e
    LEFT JOIN user owner ON owner.userId = e.userId
    LEFT JOIN department d ON d.departmentId = e.departmentId
    WHERE e.eventId = :eventId
    """)
    EventDetail getEventDetailDt2o(@Param("eventId") Long eventId , @Param("viewerId") Long viewerId);

}
