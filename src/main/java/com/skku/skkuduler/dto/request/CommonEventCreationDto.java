package com.skku.skkuduler.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonEventCreationDto {
    private List<EventInfo> events;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EventInfo {
        @NotBlank
        private String title;
        @NotBlank
        private String content;
        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime startDateTime;
        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime endDateTime;
    }
}