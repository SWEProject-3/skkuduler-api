package com.skku.skkuduler.domain.calender;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "calendar_event")
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"event_id", "calendar_id"})
        }
)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CalendarEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calendarEventId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

}
