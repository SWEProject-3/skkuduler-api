package com.skku.skkuduler.infrastructure;

import com.skku.skkuduler.domain.calender.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalenderRepository extends JpaRepository<Calendar,Long> {

}
