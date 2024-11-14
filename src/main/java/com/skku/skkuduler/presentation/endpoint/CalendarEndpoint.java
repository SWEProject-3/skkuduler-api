package com.skku.skkuduler.presentation.endpoint;

import com.skku.skkuduler.application.AuthService;
import com.skku.skkuduler.application.CalendarService;
import com.skku.skkuduler.common.exception.Error;
import com.skku.skkuduler.common.exception.ErrorException;
import com.skku.skkuduler.common.security.JwtUtil;
import com.skku.skkuduler.dto.request.*;
import com.skku.skkuduler.dto.response.CalendarEventDetailDto;
import com.skku.skkuduler.dto.response.CalendarEventSummaryDto;
import com.skku.skkuduler.presentation.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CalendarEndpoint {

    private final JwtUtil jwtUtil;
    private final CalendarService calendarService;
    private final AuthService authService;

    //TODO -> 권한 체크 (친구)
    @GetMapping("/users/{userId}/calendars/events")
    public ApiResponse<List<CalendarEventSummaryDto>> getsUserCalenderEvents(@PathVariable("userId") Long userId,
                                                                             @RequestParam(value = "rangeSearch", defaultValue = "false") boolean rangeSearch,
                                                                             @RequestParam(required = false, name = "startDate") LocalDate startDate,
                                                                             @RequestParam(required = false, name = "endDate") LocalDate endDate,
                                                                             @RequestParam(required = false, name = "yearMonth") YearMonth yearMonth,
                                                                             @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (!rangeSearch) {
            if (yearMonth == null) throw new ErrorException(Error.INVALID_URL_PARAMETERS);
            startDate = yearMonth.atDay(1);
            endDate = yearMonth.atEndOfMonth();
        } else {
            if (startDate == null || endDate == null) throw new ErrorException(Error.INVALID_URL_PARAMETERS);
            if (startDate.isAfter(endDate)) throw new ErrorException(Error.INVALID_DATE_RANGE);
        }
        return new ApiResponse<>(calendarService.getEventsBetween(null, userId, startDate, endDate));
    }

    @GetMapping("/departments/{departmentId}/calendars/events")
    public ApiResponse<List<CalendarEventSummaryDto>> getsDeptCalenderEvents(@PathVariable("departmentId") Long departmentId,
                                                                             @RequestParam(value = "rangeSearch", defaultValue = "false") boolean rangeSearch,
                                                                             @RequestParam(required = false, name = "startDate") LocalDate startDate,
                                                                             @RequestParam(required = false, name = "endDate") LocalDate endDate,
                                                                             @RequestParam(required = false, name = "yearMonth") YearMonth yearMonth,
                                                                             @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (!rangeSearch) {
            if (yearMonth == null) throw new ErrorException(Error.INVALID_URL_PARAMETERS);
            startDate = yearMonth.atDay(1);
            endDate = yearMonth.atEndOfMonth();
        } else {
            if (startDate == null || endDate == null) throw new ErrorException(Error.INVALID_URL_PARAMETERS);
            if (startDate.isAfter(endDate)) throw new ErrorException(Error.INVALID_DATE_RANGE);
        }
        return new ApiResponse<>(calendarService.getEventsBetween(departmentId, null, startDate, endDate));
    }

    // 개인 일정 생성하여, 달력에 추가하기
    @PostMapping("/users/calendars/events")
    public ApiResponse<Void> createCalenderEvent(@Valid EventCreationDto eventCreationDto,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtUtil.extractUserId(token);
        calendarService.createUserCalenderEvent(userId, eventCreationDto);
        return new ApiResponse<>("일정이 성공적으로 생성 되었습니다.");
    }

    // 외부일정 가져오기
    @PostMapping("/users/calendars/events/{eventId}")
    public ApiResponse<Void> addCalenderEvent(@PathVariable Long eventId,
                                              @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtUtil.extractUserId(token);
        calendarService.addUserCalenderEvent(userId, eventId);
        return new ApiResponse<>("일정이 성공적으로 추가 되었습니다.");
    }

    @PutMapping("/users/calendars/events/{eventId}")
    public ApiResponse<Void> updateCalenderEvent(@Valid EventUpdateDto eventUpdateDto,
                                                 @PathVariable Long eventId,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtUtil.extractUserId(token);
        authService.checkAuthForWritingEvent(eventId, userId);
        calendarService.updateUserCalenderEvent(eventId, eventUpdateDto);
        return new ApiResponse<>("일정이 성공적으로 수정되었습니다.");
    }

    @DeleteMapping("/users/calendars/events/{eventId}")
    public ApiResponse<Void> deleteCalenderEvent(@PathVariable Long eventId,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtUtil.extractUserId(token);
        calendarService.deleteUserCalenderEvent(eventId, userId);
        return new ApiResponse<>("일정이 성공적으로 삭제되었습니다.");
    }

    //TODO -> 친구인지 체크
    //학과 이벤트인경우 그냥 보여주기, 유저 이벤트인경우 친구 / 내꺼이지 확인
    @GetMapping("/users/calendars/events/{eventId}")
    public ApiResponse<CalendarEventDetailDto> getCalenderEventDetail(
            @PathVariable Long eventId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtUtil.extractUserId(token);
        return new ApiResponse<>(calendarService.getCalenderEvent(eventId, userId));

    }


}
