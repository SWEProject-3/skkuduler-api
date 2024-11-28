package com.skku.skkuduler.infrastructure.querydsl;

import com.skku.skkuduler.dto.request.SortType;
import com.skku.skkuduler.dto.response.CalendarEventDetailDto;
import com.skku.skkuduler.dto.response.DepartmentEventSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventCustomRepository {
    CalendarEventDetailDto getEventDetail(Long evenId , Long userId);
    Page<DepartmentEventSummaryDto> getDepartmentEventSummaryDtos(List<Long> departmentIds,Long userId, SortType sortType, String query, Pageable pageable);
}
