package com.skku.skkuduler.domain.calender;

import com.skku.skkuduler.domain.event.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "calender")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Calender {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calenderId;

    private String name;

    private Long userId;

    private Long departmentId;

    private boolean isGlobal;

    @OneToMany(mappedBy = "calender", cascade = CascadeType.ALL)
    private List<CalenderEvent> calenderEvents;

    public void addEvent(Long eventId) {
        CalenderEvent calenderEvent = new CalenderEvent(null, eventId,this);
        this.calenderEvents.add(calenderEvent);
    }

    public boolean removeEvent(Long eventId){
        return calenderEvents.removeIf(calenderEvent -> calenderEvent.getEventId().equals(eventId));
    }

    public void changeName(String name){
        this.name = name;
    }

    public List<Long> getEventsId(){

    }

}
