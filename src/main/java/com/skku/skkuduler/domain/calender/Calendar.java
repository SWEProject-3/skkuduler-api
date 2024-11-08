package com.skku.skkuduler.domain.calender;

import com.skku.skkuduler.domain.department.Department;
import com.skku.skkuduler.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity(name = "calendar")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calendarId;

    @Column(nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "department_id")
    private Department department;

    //virtual column
    @Column(updatable = false, insertable = false)
    private Boolean isGlobal;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CalendarEvent> calendarEvents = new ArrayList<>();

    //팩토리
    public static Calendar of(User user) {
        return Calendar.builder()
                .user(user)
                .build();
    }
    //팩토리
    public static Calendar of(Department department) {
        return Calendar.builder()
                .department(department)
                .build();
    }

    public boolean existsEvent(Long eventId){
        return calendarEvents.stream().map(CalendarEvent::getEvent).anyMatch(event -> event.getEventId().equals(eventId));
    }

    public void addEvent(Event event) {
        CalendarEvent calendarEvent = new CalendarEvent(null, event, this);
        this.calendarEvents.add(calendarEvent);
    }

    public Optional<Event> getEvent(Long eventId) {
        return calendarEvents.stream().map(CalendarEvent::getEvent).filter(event -> event.getEventId().equals(eventId)).findFirst();
    }

    public boolean removeEvent(Event event) {
        return calendarEvents.removeIf(calendarEvent -> calendarEvent.getEvent().getEventId().equals(event.getEventId()));
    }

    public void changeName(String name) {
        this.name = name;
    }

    public List<Event> getEventsBetween(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay(); // 00:00:00
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX); // 23:59:59

        return calendarEvents.stream()
                .map(CalendarEvent::getEvent)
                .filter(event -> !event.getStartDateTime().isAfter(endDateTime) && !event.getEndDateTime().isBefore(startDateTime))
                .collect(Collectors.toList());
    }


}