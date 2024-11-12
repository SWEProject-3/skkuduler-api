package com.skku.skkuduler.application;

import com.skku.skkuduler.common.exception.Error;
import com.skku.skkuduler.common.exception.ErrorException;
import com.skku.skkuduler.domain.calender.Calendar;
import com.skku.skkuduler.domain.calender.Event;
import com.skku.skkuduler.domain.calender.Image;
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
import java.util.List;

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

                ? userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException(Error.USER_NOT_FOUND))
                .getCalendar()

                : departmentRepository.findById(departmentId)
                .orElseThrow(()-> new ErrorException(Error.DEPARTMENT_NOT_FOUND))
                .getCalendar();

        return calendar.getEventsBetween(startDate, endDate).stream().map(
                event ->
                        new CalendarEventSummaryDto(
                                event.getEventId(),
                                event.getTitle(),
                                event.getColorCode(),
                                event.getStartDateTime(),
                                event.getEndDateTime()
                        )
        ).toList();
    }

    @Transactional
    public void createUserCalenderEvent(Long userId, EventCreationDto eventCreationDto) {
        Calendar calendar = userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException(Error.USER_NOT_FOUND))
                .getCalendar();
        Event event = Event.userEventOf(userId);
        event.changeTitle(eventCreationDto.getTitle());
        event.changeContent(eventCreationDto.getContent());
        event.changeColorCode(eventCreationDto.getColorCode());
        event.changeDate(eventCreationDto.getStartDateTime(), eventCreationDto.getEndDateTime());
        if (eventCreationDto.getImages() != null) {
            List<Image> images = eventCreationDto.getImages().stream()
                    .map(imageInfo -> {
                        try {
                            return Image.builder()
                                    .event(event)
                                    .src(fileService.storeFile(imageInfo.getImageFile()))
                                    .order(imageInfo.getOrder())
                                    .build();
                        } catch (IOException e) {
                            throw new ErrorException(Error.FILE_STORE_ERROR);
                        }
                    })
                    .toList();
            event.changeImages(images);
        }
        calendar.addEvent(event);
    }

    @Transactional
    public void addUserCalenderEvent(Long userId, Long eventId) {
        Calendar calendar = userRepository.findById(userId)
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
    public void deleteUserCalenderEvent(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ErrorException(Error.EVENT_NOT_FOUND));
        Calendar calendar = userRepository.findById(userId)
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

    //유저가 유저가 생성한 일정 수정 -> 어드민은 따로 만들 예쩡
    @Transactional
    public void updateUserCalenderEvent(Long eventId, EventUpdateDto eventUpdateDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ErrorException(Error.EVENT_NOT_FOUND));
        event.changeTitle(eventUpdateDto.getTitle());
        event.changeContent(eventUpdateDto.getContent());
        event.changeColorCode(eventUpdateDto.getColorCode());
        event.changeDate(eventUpdateDto.getStartDateTime(), eventUpdateDto.getEndDateTime());
        List<Image> images = eventUpdateDto.getImages() == null ? null : eventUpdateDto.getImages().stream()
                .map(imageInfo -> {
                    try {
                        return Image.builder()
                                .event(event)
                                .src(fileService.storeFile(imageInfo.getImageFile()))
                                .order(imageInfo.getOrder())
                                .build();
                    } catch (IOException e) {
                        throw new ErrorException(Error.FILE_STORE_ERROR);
                    }
                })
                .toList();
        event.changeImages(images);
    }

    @Transactional(readOnly = true)
    public CalendarEventDetailDto getCalenderEvent(Long eventId ,Long userId) {
        CalendarEventDetailDto response = eventRepository.getEventDetail(eventId,userId);
        if(response == null) throw new ErrorException(Error.EVENT_NOT_FOUND);
        response.setImages(response.getImages().stream().map(imageInfo -> new CalendarEventDetailDto.ImageInfo(baseUrl + imageInfo.getImageUrl() , imageInfo.getOrder())).toList());
        return response;
    }
}
