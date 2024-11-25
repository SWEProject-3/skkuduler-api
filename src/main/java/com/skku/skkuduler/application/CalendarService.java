package com.skku.skkuduler.application;

import com.skku.skkuduler.common.exception.Error;
import com.skku.skkuduler.common.exception.ErrorException;
import com.skku.skkuduler.domain.calender.Calendar;
import com.skku.skkuduler.domain.calender.Event;
import com.skku.skkuduler.domain.calender.Image;
import com.skku.skkuduler.dto.request.CommonEventCreationDto;
import com.skku.skkuduler.dto.request.EventCreationDto;
import com.skku.skkuduler.dto.request.EventUpdateDto;
import com.skku.skkuduler.dto.response.CalendarEventDetailDto;
import com.skku.skkuduler.dto.response.CalendarEventSummaryDto;
import com.skku.skkuduler.infrastructure.CalenderRepository;
import com.skku.skkuduler.infrastructure.DepartmentRepository;
import com.skku.skkuduler.infrastructure.EventRepository;
import com.skku.skkuduler.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalenderRepository calenderRepository;
    private final EventRepository eventRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    @Value("${spring.cloud.gcp.storage.path}${spring.cloud.gcp.storage.bucket}/")
    private String baseUrl;


    @Transactional(readOnly = true)
    public List<CalendarEventSummaryDto> getEventsBetween(Long departmentId, Long userId, LocalDate startDate, LocalDate endDate) {
        Calendar calendar = userId != null

                ? userRepository.findByIdFetchCalendar(userId)
                .orElseThrow(() -> new ErrorException(Error.USER_NOT_FOUND))
                .getCalendar()

                : departmentRepository.findByIdFetchCalendar(departmentId)
                .orElseThrow(() -> new ErrorException(Error.DEPARTMENT_NOT_FOUND))
                .getCalendar();
        LocalDateTime start = startDate.atStartOfDay(); // 00:00:00
        LocalDateTime end = endDate.atTime(LocalTime.MAX); // 23:59:59

        List<CalendarEventSummaryDto> commonEvents = eventRepository.findCommonDepartmentEvents().stream()
                .filter(event -> !event.getStartDateTime().isAfter(end) && !event.getEndDateTime().isBefore(start))
                .map(event -> new CalendarEventSummaryDto(
                        event.getEventId(),
                        event.getTitle(),
                        event.getColorCode(),
                        event.getStartDateTime(),
                        event.getEndDateTime()
                )).toList();


        List<CalendarEventSummaryDto> deptEvent = calendar.getEventsBetween(startDate, endDate).stream()
                .map(
                        event -> new CalendarEventSummaryDto(
                                event.getEventId(),
                                event.getTitle(),
                                event.getColorCode(),
                                event.getStartDateTime(),
                                event.getEndDateTime()
                        )
                )
                .collect(Collectors.toList());
        deptEvent.addAll(commonEvents);
        return deptEvent;
    }

    @Transactional
    public void createUserCalendarEvent(Long userId, EventCreationDto eventCreationDto) {
        createCalendarEvent(userId, null, eventCreationDto);
    }

    @Transactional
    public void createDepartmentCalendarEvent(Long departmentId, EventCreationDto eventCreationDto) {
        createCalendarEvent(null, departmentId, eventCreationDto);
    }

    private void createCalendarEvent(Long userId, Long departmentId, EventCreationDto eventCreationDto) {
        Calendar calendar = userId != null
                ? userRepository.findByIdFetchCalendar(userId)
                .orElseThrow(() -> new ErrorException(Error.USER_NOT_FOUND))
                .getCalendar()
                : departmentRepository.findByIdFetchCalendar(departmentId)
                .orElseThrow(() -> new ErrorException(Error.DEPARTMENT_NOT_FOUND))
                .getCalendar();

        Event event = userId != null ? Event.userEventOf(userId) : Event.deptEventOf(departmentId);
        event.changeTitle(eventCreationDto.getTitle());
        event.changeContent(eventCreationDto.getContent());
        event.changeColorCode(eventCreationDto.getColorCode());
        event.changeDate(eventCreationDto.getStartDateTime(), eventCreationDto.getEndDateTime());

        if (eventCreationDto.getImageFile() != null) {
            Image image = Image.builder()
                    .event(event)
                    .src(fileService.storeFile(eventCreationDto.getImageFile()))
                    .build();
            event.changeImage(image);
        }
        calendar.addEvent(event);
    }

    @Transactional
    public void addUserCalendarEvent(Long userId, Long eventId) {
        Calendar calendar = userRepository.findByIdFetchCalendar(userId)
                .orElseThrow(() -> new ErrorException(Error.USER_NOT_FOUND))
                .getCalendar();
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ErrorException(Error.EVENT_NOT_FOUND));
        try {
            calendar.addEvent(event);
            calenderRepository.save(calendar);
        } catch (Exception e) {
            throw new ErrorException(Error.EVENT_ALREADY_EXISTS);
        }
    }

    //유저가 일정 삭제 -> 본인 것은 영구삭제
    @Transactional
    public void deleteUserCalendarEvent(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ErrorException(Error.EVENT_NOT_FOUND));
        Calendar calendar = userRepository.findByIdFetchCalendar(userId)
                .orElseThrow(() -> new ErrorException(Error.USER_NOT_FOUND))
                .getCalendar();
        if (!calendar.removeEvent(event)) {
            throw new ErrorException(Error.INVALID_REMOVAL_CALENDER_EVENT);
        }
        ;
        calenderRepository.save(calendar);
        if (event.getIsUserEvent() && event.getUserId().equals(userId)) {
            eventRepository.delete(event);
        }
    }

    @Transactional
    public void deleteDepartmentCalendarEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ErrorException(Error.EVENT_NOT_FOUND));
        Calendar calendar = departmentRepository.findByIdFetchCalendar(event.getDepartmentId())
                .orElseThrow(() -> new ErrorException(Error.DEPARTMENT_NOT_FOUND))
                .getCalendar();
        if (!calendar.removeEvent(event)) {
            throw new ErrorException(Error.INVALID_REMOVAL_CALENDER_EVENT);
        }
        calenderRepository.save(calendar);
        eventRepository.delete(event);
    }

    @Transactional
    public void updateCalendarEvent(Long eventId, EventUpdateDto eventUpdateDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ErrorException(Error.EVENT_NOT_FOUND));
        event.changeTitle(eventUpdateDto.getTitle());
        event.changeContent(eventUpdateDto.getContent());
        event.changeColorCode(eventUpdateDto.getColorCode());
        event.changeDate(eventUpdateDto.getStartDateTime(), eventUpdateDto.getEndDateTime());
        if (eventUpdateDto.getImageFile() != null) {
            Image image = Image.builder()
                    .event(event)
                    .src(fileService.storeFile(eventUpdateDto.getImageFile()))
                    .build();
            event.changeImage(image);
        }
        eventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public CalendarEventDetailDto getCalendarEvent(Long eventId, Long userId) {
        CalendarEventDetailDto response = eventRepository.getEventDetail(eventId, userId);
        if (response == null) throw new ErrorException(Error.EVENT_NOT_FOUND);
        response.setImageUrl(baseUrl + response.getImageUrl());
        return response;
    }

    @Transactional
    public void createCommonDepartmentCalendarEventAll(CommonEventCreationDto eventCreationDtos) {
        List<Event> insertedData = eventCreationDtos.getEvents().stream()
                .map(eventCreationDto -> {
                    Event event = Event.deptEventOf(null); //학사 event 생성
                    event.changeTitle(eventCreationDto.getTitle());
                    event.changeContent(eventCreationDto.getContent());
                    event.changeDate(eventCreationDto.getStartDateTime(), eventCreationDto.getEndDateTime());
                    event.changeColorCode("#004028"); //학교 색상
                    return event;
                }).toList();
        eventRepository.saveAll(insertedData);
    }
}
