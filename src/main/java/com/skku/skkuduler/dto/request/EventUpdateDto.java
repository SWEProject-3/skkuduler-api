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
public class EventUpdateDto {
    private String title;
    private String content;
    private String colorCode;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<ImageInfo> images;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ImageInfo{
        private MultipartFile imageFile;
        private Integer order;
    }
}
