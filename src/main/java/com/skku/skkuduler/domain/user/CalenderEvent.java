package com.skku.skkuduler.domain.user;

import com.skku.skkuduler.domain.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CalenderEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calenderEventId;

    @ManyToOne
    @JoinColumn(name = "calender_id")
    private Calender calender;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
