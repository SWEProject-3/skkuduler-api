package com.skku.skkuduler.domain.calender;

import com.skku.skkuduler.domain.event.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "calender_event")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CalenderEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calenderEventId;

    private Long eventId;

    @ManyToOne
    @JoinColumn(name = "calender_id")
    private Calender calender;
}
