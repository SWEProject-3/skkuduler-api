package com.skku.skkuduler.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentEventSummaryDto {

    private DepartmentInfo departmentInfo;
    private EventInfo eventInfo;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DepartmentInfo{
        private Long departmentId;
        private String departmentName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EventInfo{
        private Long eventId;
        private String title;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime startDateTime;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime endDateTime;
        private Long likeCount;
        private Long commentCount;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime createdAt;
    }
}
