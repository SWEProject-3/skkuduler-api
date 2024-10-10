package com.skku.skkuduler.infrastructure;

import com.skku.skkuduler.domain.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Long> {
}
