package com.skku.skkuduler.presentation.endpoint;

import com.skku.skkuduler.application.AuthService;
import com.skku.skkuduler.application.CalenderService;
import com.skku.skkuduler.common.exception.Error;
import com.skku.skkuduler.common.exception.ErrorException;
import com.skku.skkuduler.common.security.JwtUtil;
import com.skku.skkuduler.dto.request.*;
import com.skku.skkuduler.dto.response.CalenderEventDetailDto;
import com.skku.skkuduler.dto.response.CalenderInfoDto;
import com.skku.skkuduler.dto.response.EventSummaryDto;
import com.skku.skkuduler.presentation.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class CalenderEndpoint {

    private final JwtUtil jwtUtil;
    private final CalenderService calenderService;
    private final AuthService authService;

    //TODO -> 권한 체크 (친구)
    @GetMapping("/{userId}/calenders/events")
    public ApiResponse<List<EventSummaryDto>> getsCalenderEvents(@PathVariable("userId") Long userId,
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
        return new ApiResponse<>(calenderService.getEventsBetween(userId, startDate, endDate));
    }

    // 개인 일정 생성하여, 달력에 추가하기
    @PostMapping("/calenders/events")
    public ApiResponse<Void> createCalenderEvent(EventCreationDto eventCreationDto,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtUtil.extractUserId(token);
        calenderService.createUserCalenderEvent(userId, eventCreationDto);
        return new ApiResponse<>("일정이 성공적으로 생성 되었습니다.");
    }

    // 외부일정 가져오기
    @PostMapping("/calenders/events/{eventId}")
    public ApiResponse<Void> addCalenderEvent(@PathVariable Long eventId,
                                              @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtUtil.extractUserId(token);
        calenderService.addUserCalenderEvent(userId, eventId);
        return new ApiResponse<>("일정이 성공적으로 추가 되었습니다.");
    }

    @PutMapping("/calenders/events/{eventId}")
    public ApiResponse<Void> updateCalenderEvent(EventUpdateDto eventUpdateDto,
                                                 @PathVariable Long eventId,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtUtil.extractUserId(token);
        authService.checkAuthForWritingEvent(eventId, userId);
        calenderService.updateUserCalenderEvent(eventId, eventUpdateDto);
        return new ApiResponse<>("일정이 성공적으로 수정되었습니다.");
    }

    @DeleteMapping("/calenders/events/{eventId}")
    public ApiResponse<Void> deleteCalenderEvent(@PathVariable Long eventId,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtUtil.extractUserId(token);
        calenderService.deleteUserCalenderEvent(eventId, userId);
        return new ApiResponse<>("일정이 성공적으로 삭제되었습니다.");
    }

    //TODO -> 친구인지 체크
    //학과 이벤트인경우 그냥 보여주기, 유저 이벤트인경우 친구 / 내꺼이지 확인
    @GetMapping("/calenders/events/{eventId}")
    public ApiResponse<CalenderEventDetailDto> getCalenderEventDetail(
                                                                      @PathVariable Long eventId,
                                                                      @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long viewerID = jwtUtil.extractUserId(token);
        return new ApiResponse<>(calenderService.getCalenderEvent(eventId));

    }

}
