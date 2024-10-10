package com.skku.skkuduler.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskDetailDto {
    private String title;
    private int priority; // 1-5
    private String createdBy;
    private String description;
}
