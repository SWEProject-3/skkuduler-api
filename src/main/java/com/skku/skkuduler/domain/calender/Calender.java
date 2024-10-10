package com.skku.skkuduler.domain.calender;

import com.skku.skkuduler.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Calender {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calenderId;

    private String name;

    private Long userId;

    @OneToMany(mappedBy = "calender",cascade = CascadeType.ALL)
    private List<CalenderEvent> calenderEvents;

    public void addEvent(Long eventId) {
        CalenderEvent calenderEvent = new CalenderEvent(null, eventId);
        this.calenderEvents.add(calenderEvent);
    }

}
