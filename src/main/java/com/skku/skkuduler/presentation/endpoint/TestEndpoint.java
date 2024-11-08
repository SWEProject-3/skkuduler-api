package com.skku.skkuduler.presentation.endpoint;

import com.skku.skkuduler.dto.EventDetail;
import com.skku.skkuduler.dto.response.CalendarEventDetailDto;
import com.skku.skkuduler.infrastructure.DepartmentRepository;
import com.skku.skkuduler.infrastructure.EventRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestEndpoint {

    private final DepartmentRepository departmentRepository;
    private final EventRepository eventRepository;

    public TestEndpoint(DepartmentRepository departmentRepository, EventRepository eventRepository) {
        this.departmentRepository = departmentRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping
    public EventDetail f1(){
        return eventRepository.getEventDetailDt2o(3L,1L);
    }

}
