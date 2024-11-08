package com.skku.skkuduler.dto.response;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.List;

public interface EventDetail {
    Boolean getIsDepartmentEvent();
    Boolean getIsMyEvent();
    Boolean getIsAddedToMyCalendar();
    Long getOwnerUserId();
    String getOwnerName();
    Long getDepartmentId();
    String getDepartmentName();
    EventInfo getEventInfo();
    List<ImageInfo> getImages();

    interface EventInfo {
        Long getEventId();
        String getTitle();
        String getContent();
        String getColorCode();
        LocalDateTime getStartDateTime();
        LocalDateTime getEndDateTime();
    }

    interface ImageInfo {
        String getImageUrl();
        int getOrder();
    }
}
