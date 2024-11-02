package com.skku.skkuduler.domain.calender;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity(name = "calender")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Calender {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calenderId;

    @Column(nullable = false)
    private String name;

    @Column(updatable = false)
    private Long userId;

    @Column(updatable = false)
    private Long departmentId;

    @Column(updatable = false)
    private boolean isGlobal;

    @OneToMany(mappedBy = "calender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CalenderEvent> calenderEvents = new ArrayList<>();

    //팩토리
    public static Calender userCalender(Long userId) {
        return Calender.builder()
                .userId(userId)
                .isGlobal(false)
                .build();
    }
    //팩토리
    public static Calender deptCalender(Long departmentId) {
        return Calender.builder()
                .departmentId(departmentId)
                .isGlobal(true)
                .build();
    }

    public void addEvent(Event event) {
        CalenderEvent calenderEvent = new CalenderEvent(null, event, this);
        this.calenderEvents.add(calenderEvent);
    }

    public Optional<Event> getEvent(Long eventId) {
        return calenderEvents.stream().map(CalenderEvent::getEvent).filter(event -> event.getEventId().equals(eventId)).findFirst();
    }

    public boolean removeEvent(Event event) {
        return calenderEvents.removeIf(calenderEvent -> calenderEvent.getEvent().getEventId().equals(event.getEventId()));
    }

    public void changeName(String name) {
        this.name = name;
    }

    public List<Event> getEventsBetween(LocalDate startDate, LocalDate endDate) {
        return calenderEvents.stream()
                .map(CalenderEvent::getEvent)
                .filter(event -> !event.getStartDate().isAfter(endDate) && !event.getEndDate().isBefore(startDate))
                .collect(Collectors.toList());
    }


}