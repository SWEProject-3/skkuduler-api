package com.skku.skkuduler.presentation.endpoint;

import com.skku.skkuduler.application.CalendarService;
import com.skku.skkuduler.application.PermissionService;
import com.skku.skkuduler.common.exception.Error;
import com.skku.skkuduler.common.exception.ErrorException;
import com.skku.skkuduler.common.security.JwtUtil;
import com.skku.skkuduler.domain.user.User;
import com.skku.skkuduler.dto.request.EventCreationDto;
import com.skku.skkuduler.dto.request.EventUpdateDto;
import com.skku.skkuduler.presentation.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminEndpoint {
    private final JwtUtil jwtUtil;
    private final CalendarService calendarService;
    private final PermissionService permissionService;

    // 학과 일정 등록
    @PostMapping("/departments/{departmentId}/calendars/events")
    public ApiResponse<Void> createDepartmentEvent(@PathVariable("departmentId") Long departmentId,
                                                   EventCreationDto eventCreationDto,
                                                   @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtUtil.extractUserId(token);
        if(jwtUtil.extractRole(token) != User.Role.ADMIN){
            throw new ErrorException(Error.PERMISSION_DENIED);
        }
        if(!permissionService.hasDepartmentPermission(userId,departmentId)){
            throw new ErrorException(Error.PERMISSION_DENIED);
        }
        calendarService.createDepartmentCalendarEvent(departmentId,eventCreationDto);
        return new ApiResponse<>("학과 일정이 성공적으로 생성되었습니다.");

    }
    // 학과 일정 수정
    @PutMapping("/departments/calendars/events/{eventId}")
    public ApiResponse<Void> updateDepartmentEvent(@PathVariable("eventId") Long eventId,
                                                   EventUpdateDto eventUpdateDto,
                                                   @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtUtil.extractUserId(token);
        if(jwtUtil.extractRole(token) != User.Role.ADMIN){
            throw new ErrorException(Error.PERMISSION_DENIED);
        }
        if(!permissionService.hasEventPermission(userId, eventId)){
            throw new ErrorException(Error.PERMISSION_DENIED);
        }
        calendarService.updateCalendarEvent(eventId,eventUpdateDto);
        return new ApiResponse<>("학과 일정이 성공적으로 수정되었습니다.");

    }
    // 학과 일정 삭제
    @DeleteMapping("/departments/calendars/events/{eventId}")
    public ApiResponse<Void> deleteDepartmentEvent(@PathVariable("eventId") Long eventId,
                                                   @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtUtil.extractUserId(token);
        if(jwtUtil.extractRole(token) != User.Role.ADMIN){
            throw new ErrorException(Error.PERMISSION_DENIED);
        }
        if(!permissionService.hasEventPermission(userId, eventId)){
            throw new ErrorException(Error.PERMISSION_DENIED);
        }
        calendarService.deleteDepartmentCalendarEvent(eventId);
        return new ApiResponse<>("학과 일정이 성공적으로 삭제되었습니다.");
    }
}
