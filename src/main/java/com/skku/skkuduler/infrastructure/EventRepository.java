package com.skku.skkuduler.infrastructure;

import com.skku.skkuduler.domain.calender.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Long> {
}
