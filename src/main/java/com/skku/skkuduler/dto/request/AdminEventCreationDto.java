package com.skku.skkuduler.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminEventCreationDto {
    private String title;
    private String content;
    private String colorCode;
    private List<Images> images; //이미지 정보
    private LocalDate startDate;
    private LocalDate endDate;

    static class Images {
        private MultipartFile imageFile;
        private int order;
    }
}

