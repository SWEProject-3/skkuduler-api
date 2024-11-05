package com.skku.skkuduler.infrastructure;

import com.skku.skkuduler.domain.calender.Calender;
import com.skku.skkuduler.dto.response.CalenderInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CalenderRepository extends JpaRepository<Calender,Long> {

}
