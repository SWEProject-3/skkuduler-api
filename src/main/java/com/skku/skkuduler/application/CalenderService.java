package com.skku.skkuduler.application;

import com.skku.skkuduler.common.exception.Error;
import com.skku.skkuduler.common.exception.ErrorException;
import com.skku.skkuduler.domain.calender.Calender;
import com.skku.skkuduler.domain.calender.Event;
import com.skku.skkuduler.domain.calender.Image;
import com.skku.skkuduler.dto.request.EventCreationDto;
import com.skku.skkuduler.dto.request.EventUpdateDto;
import com.skku.skkuduler.dto.response.CalenderEventDetailDto;
import com.skku.skkuduler.dto.response.CalenderInfoDto;
import com.skku.skkuduler.dto.response.EventSummaryDto;
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

import static com.skku.skkuduler.dto.response.CalenderEventDetailDto.*;

@Service
@RequiredArgsConstructor
public class CalenderService {

    private final CalenderRepository calenderRepository;
    private final EventRepository eventRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    @Value("${spring.cloud.gcp.storage.path}${spring.cloud.gcp.storage.bucket}/")
    private String baseUrl;

    @Transactional(readOnly = true)
    public List<CalenderInfoDto> loadUserCalenders(Long userId) {
        return calenderRepository.findAllUserCalendersToCalenderInfoDto(userId);
    }

    @Transactional(readOnly = true)
    public List<CalenderInfoDto> loadDepartmentCalenders(Long departmentId) {
        return calenderRepository.findAllDeptCalendersToCalenderInfoDto(departmentId);
    }

    @Transactional
    public void createUserCalender(Long userId, String name) {
        Calender calender = Calender.userCalender(userId);
        calender.changeName(name);
        calenderRepository.save(calender);
    }

    @Transactional
    public void updateUserCalender(Long calenderId, String name) {
        Calender calender = calenderRepository.findById(calenderId).orElseThrow(() -> new ErrorException(Error.CALENDER_NOT_FOUND));
        calender.changeName(name);
        calenderRepository.save(calender);
    }

    @Transactional
    public void deleteUserCalender(Long calenderId) {
        Calender calender = calenderRepository.findById(calenderId).orElseThrow(() -> new ErrorException(Error.CALENDER_NOT_FOUND));
        calenderRepository.delete(calender);
    }

    @Transactional(readOnly = true)
    public List<EventSummaryDto> getEventsBetween(Long calenderId, LocalDate startDate, LocalDate endDate) {
        Calender calender = calenderRepository.findById(calenderId).orElseThrow(() -> new ErrorException(Error.CALENDER_NOT_FOUND));
        return calender.getEventsBetween(startDate, endDate).stream().map(
                event ->
                        new EventSummaryDto(
                                event.getEventId(),
                                event.getTitle(),
                                event.getColorCode(),
                                event.getStartDate(),
                                event.getEndDate().isAfter(endDate) ? endDate : event.getEndDate()
                        )
        ).toList();
    }

    @Transactional
    public void createUserCalenderEvent(Long calenderId, EventCreationDto eventCreationDto) {
        Calender calender = calenderRepository.findById(calenderId).orElseThrow(() -> new ErrorException(Error.CALENDER_NOT_FOUND));
        Event event = Event.userEventOf(calender.getUserId());
        event.changeTitle(eventCreationDto.getTitle());
        event.changeContent(eventCreationDto.getContent());
        event.changeColorCode(eventCreationDto.getColorCode());
        event.changeDate(eventCreationDto.getStartDate(), eventCreationDto.getEndDate());
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
        calender.addEvent(event);
    }

    @Transactional
    public void addUserCalenderEvent(Long calenderId, Long fromCalenderId, Long eventId) {
        Calender calender = calenderRepository.findById(calenderId).orElseThrow(() -> new ErrorException(Error.CALENDER_NOT_FOUND));
        Calender fromCalender = calenderRepository.findById(fromCalenderId).orElseThrow(() -> new ErrorException(Error.CALENDER_NOT_FOUND));
        Event event = fromCalender.getEvent(eventId).orElseThrow(() -> new ErrorException(Error.EVENT_NOT_FOUND));
        try {
            calender.addEvent(event);
            calenderRepository.save(calender);
        } catch (Exception e) {
            throw new ErrorException(Error.EVENT_ALREADY_EXISTS);
        }
    }

    //유저가 일정 삭제 -> 본인 것은 영구삭제
    @Transactional
    public void deleteUserCalenderEvent(Long calenderId, Long eventId) {
        Calender calender = calenderRepository.findById(calenderId).orElseThrow(() -> new ErrorException(Error.CALENDER_NOT_FOUND));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ErrorException(Error.EVENT_NOT_FOUND));
        if(!calender.removeEvent(event)){
            throw new ErrorException(Error.INVALID_REMOVAL_CALENDER_EVENT);
        };
        calenderRepository.save(calender);
        if (event.getIsUserEvent() && event.getUserId().equals(calender.getUserId())) {
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
        event.changeDate(eventUpdateDto.getStartDate(), eventUpdateDto.getEndDate());
        List<Image> images = eventUpdateDto.getImages().stream()
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
    public CalenderEventDetailDto getCalenderEvent(Long calenderId, Long eventId) {
        Calender calender = calenderRepository.findById(calenderId).orElseThrow(() -> new ErrorException(Error.CALENDER_NOT_FOUND));
        Event calenderEvent = calender.getEvent(eventId).orElseThrow(() -> new ErrorException(Error.EVENT_NOT_FOUND));

        EventInfo eventInfo = new EventInfo(calenderEvent.getEventId(), calenderEvent.getTitle(), calenderEvent.getContent()
                , calenderEvent.getColorCode(), calenderEvent.getStartDate(), calenderEvent.getEndDate());

        List<ImageInfo> images = calenderEvent.getImages().stream()
                .map(image -> new ImageInfo( baseUrl + image.getSrc(), image.getOrder()))
                .toList();

        CalenderEventDetailDto calenderEventDetailDto = builder()
                .eventInfo(eventInfo)
                .images(images)
                .build();

        if (calenderEvent.getIsUserEvent()) { // 유저 이벤트인경우
            calenderEventDetailDto.setIsDepartmentEvent(false);
            String userName = userRepository.findById(calenderEvent.getUserId()).orElseThrow(() -> new ErrorException(Error.USER_NOT_FOUND)).getName();
            calenderEventDetailDto.setOwnerName(userName);
            calenderEventDetailDto.setOwnerUserId(calenderEvent.getUserId());
        } else { // 학과 이벤트인 경우
            calenderEventDetailDto.setIsDepartmentEvent(true);
            String departmentName = departmentRepository.findById(calender.getDepartmentId()).orElseThrow(()-> new ErrorException(Error.DEPARTMENT_NOT_FOUND)).getName();
            calenderEventDetailDto.setDepartmentName(departmentName);
            calenderEventDetailDto.setDepartmentId(calenderEvent.getDepartmentId());
        }

        return calenderEventDetailDto;
    }
}
