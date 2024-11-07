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
public class CalendarEventDetailDto {
    private Boolean isDepartmentEvent;
    private Long ownerUserId;
    private String ownerName;
    private Long departmentId;
    private String departmentName;
    private EventInfo eventInfo;
    private List<ImageInfo> images;

    @Data
    @AllArgsConstructor
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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ImageInfo{
        private String imageUrl;
        private int order;
    }
}
