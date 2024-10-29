package com.skku.skkuduler.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CalenderEventDetailDto {
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
        private LocalDate startDate;
        private LocalDate endDate;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ImageInfo{
        private String imageUrl;
        private int order;
    }
}
