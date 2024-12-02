package com.skku.skkuduler.presentation.endpoint;

import com.skku.skkuduler.application.NotificationService;
import com.skku.skkuduler.domain.calender.Event;
import com.skku.skkuduler.dto.response.FriendInfoDto;
import com.skku.skkuduler.presentation.ApiResponse;
import com.skku.skkuduler.common.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationEndpoint {

    private final NotificationService notificationService;
    private final JwtUtil jwtUtil;

    @GetMapping("/notifications")
    public ApiResponse<Map<String, Object>> getNotifications(@RequestHeader(name = "Authorization") String token) {
        Long userId = jwtUtil.extractUserId(token);

        // PENDING 상태 친구 요청
        List<FriendInfoDto> pendingFriendRequests = notificationService.getPendingFriendRequests(userId);

        // 24시간 이내 종료 이벤트
        List<Event> upcomingEvents = notificationService.getUpcomingEvents(userId);

        // 결과 조합
        Map<String, Object> notifications = new HashMap<>();
        notifications.put("pendingFriendRequests", pendingFriendRequests);
        notifications.put("upcomingEvents", upcomingEvents);

        return new ApiResponse<>("success(24이내 종료 이벤트)", notifications);
    }

    @GetMapping("/today")
    public ApiResponse<List<Event>> getTodayEndingEvents(@RequestHeader(name = "Authorization") String token) {
        Long userId = jwtUtil.extractUserId(token);

        // 오늘 종료되는 이벤트 가져오기
        List<Event> todayEvents = notificationService.getTodayEndingEvents(userId);

        return new ApiResponse<>("success(오늘 종료 이벤트)", todayEvents);
    }
}
