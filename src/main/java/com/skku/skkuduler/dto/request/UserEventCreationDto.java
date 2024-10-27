package com.skku.skkuduler.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEventCreationDto {
    private String title;
    private String content;
    private String colorCode;
    private LocalDate startDate;
    private LocalDate endDate;
}
