package com.skku.skkuduler.infrastructure.querydsl;

import com.skku.skkuduler.dto.response.CalendarEventDetailDto;

public interface EventCustomRepository {
    CalendarEventDetailDto getEventDetail(Long evenId , Long userId);
}
