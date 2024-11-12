package com.skku.skkuduler.presentation.endpoint;

import com.skku.skkuduler.domain.calender.Calendar;
import com.skku.skkuduler.domain.user.User;
import com.skku.skkuduler.dto.response.CalendarEventDetailDto;
import com.skku.skkuduler.infrastructure.CalenderRepository;
import com.skku.skkuduler.infrastructure.DepartmentRepository;
import com.skku.skkuduler.infrastructure.EventRepository;
import com.skku.skkuduler.infrastructure.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public void f1(){
        User user = userRepository.findById(1L).orElseThrow();
        Calendar calender = Calendar.of(user);
        calender.changeName("test");
        calenderRepository.save(calender);
    }

}
