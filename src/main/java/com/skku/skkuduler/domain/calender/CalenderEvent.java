package com.skku.skkuduler.domain.calender;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "calender_event")
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"event_id", "calender_id"})
        }
)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CalenderEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calenderEventId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "calender_id")
    private Calender calender;

}
