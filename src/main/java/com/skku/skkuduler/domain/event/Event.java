package com.skku.skkuduler.domain.event;

import com.skku.skkuduler.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "event")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Event extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    private String title;

    private String content;

    private Long departmentId;

    private Long userId;

    private boolean isGlobal;

    private LocalDate startDate;

    private LocalDate endDate;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Image> images;
}
