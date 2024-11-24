package com.skku.skkuduler.application;

import com.skku.skkuduler.domain.calender.Calendar;
import com.skku.skkuduler.domain.calender.Event;
import com.skku.skkuduler.domain.friendship.Friendship;
import com.skku.skkuduler.domain.friendship.Friendship.FriendshipStatus;
import com.skku.skkuduler.common.exception.Error;
import com.skku.skkuduler.common.exception.ErrorException;
import com.skku.skkuduler.dto.response.FriendInfoDto;
import com.skku.skkuduler.infrastructure.CalendarRepository;
import com.skku.skkuduler.infrastructure.FriendshipRepository;
import com.skku.skkuduler.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final CalendarRepository calendarRepository;
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<FriendInfoDto> getPendingFriendRequests(Long userId) {
        // PENDING 상태의 친구 요청 가져오기
        List<Friendship> pendingRequests = friendshipRepository.findByToUserIdAndStatus(userId, FriendshipStatus.PENDING);

        return pendingRequests.stream().map(friendship -> {
            Long friendUserId = friendship.getFromUserId();
            return userRepository.findById(friendUserId).map(user ->
                    FriendInfoDto.builder()
                            .friendshipId(friendship.getFriendshipId())
                            .friendUserId(user.getUserId())
                            .friendName(user.getName())
                            .friendEmail(user.getEmail())
                            .status(friendship.getStatus())
                            .build()
            ).orElseThrow(() -> new ErrorException(Error.USER_NOT_FOUND));
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Event> getUpcomingEvents(Long userId) {
        // 사용자의 캘린더 가져오기
        Calendar userCalendar = calendarRepository.findByUser_UserId(userId).stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No calendar found for user ID: " + userId));

        LocalDateTime now = LocalDateTime.now();

        // 24시간 이내 종료되는 이벤트 필터링
        return userCalendar.getCalendarEvents().stream()
                .map(calendarEvent -> calendarEvent.getEvent())
                .filter(event -> {
                    LocalDateTime eventEndDateTime = event.getEndDateTime();
                    Duration duration = Duration.between(now, eventEndDateTime);
                    return !duration.isNegative() && duration.toHours() <= 24;
                })
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<Event> getTodayEndingEvents(Long userId) {
        // 사용자의 캘린더 가져오기
        Calendar userCalendar = calendarRepository.findByUser_UserId(userId).stream()
                .findFirst()
                .orElseThrow(() -> new ErrorException(Error.NO_CALENDAR));

        LocalDate today = LocalDate.now();

        return userCalendar.getCalendarEvents().stream()
                .map(calendarEvent -> calendarEvent.getEvent())
                .filter(event -> event.getEndDateTime().toLocalDate().equals(today))
                .collect(Collectors.toList());
    }
}
