package com.skku.skkuduler.dto;

import com.skku.skkuduler.domain.calender.Event;
import com.skku.skkuduler.domain.calender.Image;
import com.skku.skkuduler.dto.response.EventInfo;
import com.skku.skkuduler.dto.response.ImageInfo;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDetail {
    private Boolean isDepartmentEvent;
    private Boolean isMyEvent;
    private Boolean isAddedToMyCalendar;
    private Long ownerUserId;
    private String ownerName;
    private Long departmentId;
    private String departmentName;
    private Event event;
    private List<Image> images;
}
