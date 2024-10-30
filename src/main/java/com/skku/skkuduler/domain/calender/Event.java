package com.skku.skkuduler.domain.calender;

import com.skku.skkuduler.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "event")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    private String title;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<CalenderEvent> calenderEvents;

    private String content;

    private String colorCode;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean isUserEvent;

    private Long userId;

    private Long departmentId;

    @Builder.Default
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    public static Event userEventOf(Long userId){
        return Event.builder().userId(userId).isUserEvent(true).build();
    }

    public static Event deptEventOf(Long departmentId){
        return Event.builder().departmentId(departmentId).isUserEvent(false).build();
    }

    public void changeTitle(String title){
        if(title != null) this.title = title;
    }

    public void changeContent(String content){
        if(content != null) this.content = content;
    }

    public void changeColorCode(String colorCode){
        if(colorCode != null) this.colorCode = colorCode;
    }

    public void changeDate(LocalDate startDate, LocalDate endDate){
        if(startDate != null && endDate != null) {
            this.startDate = startDate;
            this.endDate = endDate;
        }
    }

    public void changeImages(List<Image> images){
        this.images.clear();
        this.images.addAll(images);
    }
}
