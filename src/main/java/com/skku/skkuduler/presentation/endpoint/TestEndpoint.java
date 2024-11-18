package com.skku.skkuduler.presentation.endpoint;

import com.skku.skkuduler.domain.calender.Calendar;
import com.skku.skkuduler.domain.user.User;
import com.skku.skkuduler.dto.request.SortType;
import com.skku.skkuduler.dto.response.CalendarEventDetailDto;
import com.skku.skkuduler.dto.response.DepartmentEventSummaryDto;
import com.skku.skkuduler.infrastructure.CalenderRepository;
import com.skku.skkuduler.infrastructure.DepartmentRepository;
import com.skku.skkuduler.infrastructure.EventRepository;
import com.skku.skkuduler.infrastructure.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestEndpoint {

    private final DepartmentRepository departmentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CalenderRepository calenderRepository;

    public TestEndpoint(DepartmentRepository departmentRepository, EventRepository eventRepository, UserRepository userRepository, CalenderRepository calenderRepository) {
        this.departmentRepository = departmentRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.calenderRepository = calenderRepository;
    }

    @GetMapping
    public Page<DepartmentEventSummaryDto> f1(@RequestParam(value = "sortBy", defaultValue = "LATEST") SortType sortType,
                                              @RequestParam(value = "query", required = false) String query){
        System.out.println(query);
        return eventRepository.getDepartmentEventSummaryDtos(List.of(1L,2L),sortType,query, PageRequest.of(0,2));
    }

}
