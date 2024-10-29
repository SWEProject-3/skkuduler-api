package com.skku.skkuduler.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventSummaryDto {
    private Long eventId;
    private String title;
    private String colorCode;
    private LocalDate startDate;
    private LocalDate endDate;
}
