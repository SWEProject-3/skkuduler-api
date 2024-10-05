package com.skku.skkuduler.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskPostRequest {
    private String title;
    private int priority; // 1-5
    private String createdBy;
    private String description;
}
