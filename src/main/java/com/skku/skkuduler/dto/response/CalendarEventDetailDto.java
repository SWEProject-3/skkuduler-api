package com.skku.skkuduler.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//TODO 좋아요 수
public class CalendarEventDetailDto {
    private Long eventId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;
    private Boolean isDepartmentEvent;
    private Boolean isCommonEvent;
    private Boolean hasPermission;
    private Boolean isAddedToMyCalendar;
    private Boolean isLiked;
    private Long ownerUserId;
    private String ownerName;
    private Long departmentId;
    private String departmentName;
    private Long likeCount;
    private EventInfo eventInfo;
    private String imageUrl;

    @Data
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class EventInfo{
        private Long eventId;
        private String title;
        private String content;
        private String colorCode;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime startDateTime;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime endDateTime;
    }


}
