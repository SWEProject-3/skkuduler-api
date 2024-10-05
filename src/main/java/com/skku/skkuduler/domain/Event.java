package com.skku.skkuduler.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Event extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    private String title;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;

    @OneToMany(mappedBy = "event")
    private List<Image> images;

    @OneToMany(mappedBy = "event")
    private List<Notification> notifications;

    @OneToMany(mappedBy = "event")
    private List<Subscribe> subscriptions;

}
