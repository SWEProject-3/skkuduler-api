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
@RequestMapping("/api/calenders")
public class CalenderEndpoint {

    private final JwtUtil jwtUtil;
    private final CalenderService calenderService;
    private final AuthService authService;

    @GetMapping
    public ApiResponse<List<CalenderInfoDto>> getCalenders(@RequestParam(required = false, name = "userId") Long userId,
                                                           @RequestParam(required = false, name = "departmentId") Long departmentId,
                                                           @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        List<CalenderInfoDto> response;
        Long viewerId = jwtUtil.extractUserId(token);
        if (userId != null) { // 해당 userId의 달력을 전부 가져옴 userId
            response = calenderService.loadUserCalenders(userId);
        } else if (departmentId != null) { //해당 학과의 달력을 전부 가져옴
            response = calenderService.loadDepartmentCalenders(departmentId);
        } else {
            //URL 파라미터가 존재하지 않을 경우
            throw new ErrorException(Error.INVALID_URL_PARAMETERS);
        }

        return new ApiResponse<>(response);
    }

    @PostMapping
    public ApiResponse<Void> createCalender(@RequestBody CalenderCreationDto calenderCreationDto,
                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        String name = calenderCreationDto.getName();
        Long userId = jwtUtil.extractUserId(token);
        calenderService.createUserCalender(userId, name);
        return new ApiResponse<>("달력이 성공적으로 생성되었습니다.");
    }

    //TODO : validation (not blank , not null?)
    @PutMapping("/{calenderId}")
    public ApiResponse<Void> updateCalender(@PathVariable("calenderId") Long calenderId,
                                            @RequestBody CalenderUpdateDto calenderUpdateDto,
                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtUtil.extractUserId(token);
        authService.checkAuthForWritingCalender(calenderId,userId);
        String name = calenderUpdateDto.getName();
        calenderService.updateUserCalender(calenderId, name);
        return new ApiResponse<>("달력이 성공적으로 수정 되었습니다.");
    }

    @DeleteMapping("/{calenderId}")
    public ApiResponse<Void> deleteCalender(@PathVariable("calenderId") Long calenderId,
                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtUtil.extractUserId(token);
        authService.checkAuthForWritingCalender(calenderId,userId);
        calenderService.deleteUserCalender(calenderId);
        return new ApiResponse<>("달력이 성공적으로 삭제 되었습니다.");
    }

    // 일정범위내의 일정 반환하기.
    @GetMapping("/{calenderId}/events")
    public ApiResponse<List<EventSummaryDto>> getsCalenderEvents(@PathVariable("calenderId") Long calenderId,
                                                                 @RequestParam(value = "rangeSearch", defaultValue = "false") boolean rangeSearch,
                                                                 @RequestParam(required = false, name = "startDate") LocalDate startDate,
                                                                 @RequestParam(required = false, name = "endDate") LocalDate endDate,
                                                                 @RequestParam(required = false, name = "yearMonth") YearMonth yearMonth,
                                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtUtil.extractUserId(token);
        authService.checkAuthForReadingCalender(calenderId, userId);
        if (!rangeSearch) {
            if (yearMonth == null) throw new ErrorException(Error.INVALID_URL_PARAMETERS);
            startDate = yearMonth.atDay(1);
            endDate = yearMonth.atEndOfMonth();
        } else {
            if (startDate == null || endDate == null) throw new ErrorException(Error.INVALID_URL_PARAMETERS);
        }
        return new ApiResponse<>(calenderService.getEventsBetween(calenderId, startDate, endDate));
    }

    // 개인 일정 생성하여, 달력에 추가하기
    @PostMapping("/{calenderId}/events")
    public ApiResponse<Void> createCalenderEvent(@PathVariable("calenderId") Long calenderId,
                                                 EventCreationDto eventCreationDto,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtUtil.extractUserId(token);
        authService.checkAuthForWritingCalender(calenderId, userId);
        calenderService.createUserCalenderEvent(calenderId, eventCreationDto);
        return new ApiResponse<>("일정이 성공적으로 생성 되었습니다.");
    }

    @PostMapping("/{calenderId}/events/{eventId}")
    public ApiResponse<Void> addCalenderEvent(@PathVariable Long calenderId,
                                              @PathVariable Long eventId,
                                              @RequestBody AddCalenderEventDto addCalenderEventDto,
                                              @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtUtil.extractUserId(token);
        authService.checkAuthForWritingCalender(calenderId, userId);
        authService.checkAuthForReadingCalender(addCalenderEventDto.getFromCalenderId(),userId);
        calenderService.addUserCalenderEvent(calenderId, addCalenderEventDto.getFromCalenderId(), eventId);
        return new ApiResponse<>("일정이 성공적으로 추가 되었습니다.");
    }

    @PutMapping("/events/{eventId}")
    public ApiResponse<Void> updateCalenderEvent(EventUpdateDto eventUpdateDto,
                                                 @PathVariable Long eventId,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtUtil.extractUserId(token);
        authService.checkAuthForWritingEvent(eventId, userId);
        calenderService.updateUserCalenderEvent(eventId, eventUpdateDto);
        return new ApiResponse<>("일정이 성공적으로 수정되었습니다.");
    }

    @DeleteMapping("/{calenderId}/events/{eventId}")
    public ApiResponse<Void> deleteCalenderEvent(@PathVariable Long calenderId,
                                                 @PathVariable Long eventId,
                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtUtil.extractUserId(token);
        authService.checkAuthForWritingCalender(calenderId, userId);
        calenderService.deleteUserCalenderEvent(calenderId, eventId);
        return new ApiResponse<>("일정이 성공적으로 삭제되었습니다.");
    }

    //학과 이벤트인경우 그냥 보여주기, 유저 이벤트인경우 친구 / 내꺼이지 확인
    @GetMapping("/{calenderId}/events/{eventId}")
    public ApiResponse<CalenderEventDetailDto> getCalenderEventDetail(@PathVariable Long calenderId,
                                                                      @PathVariable Long eventId,
                                                                      @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtUtil.extractUserId(token);
        authService.checkAuthForReadingCalender(calenderId,userId);
        return new ApiResponse<>(calenderService.getCalenderEvent(calenderId,eventId));

    }

}
