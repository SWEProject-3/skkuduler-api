package com.skku.skkuduler.domain.calender;

import com.skku.skkuduler.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "event")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    private String title;

    private String content;

    private String colorCode;

    private LocalDate startDate;

    private LocalDate endDate;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Image> images;

}
