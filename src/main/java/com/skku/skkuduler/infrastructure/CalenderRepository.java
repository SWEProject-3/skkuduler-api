package com.skku.skkuduler.infrastructure;

import com.skku.skkuduler.domain.calender.Calender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalenderRepository extends JpaRepository<Calender,Long> {
}
