package com.skku.skkuduler.infrastructure;

import com.skku.skkuduler.domain.calender.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar,Long> {
    List<Calendar> findByUser_UserId(Long userId);
}
