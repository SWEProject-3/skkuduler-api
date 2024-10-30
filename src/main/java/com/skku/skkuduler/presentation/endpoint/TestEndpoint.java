package com.skku.skkuduler.presentation.endpoint;

import com.skku.skkuduler.infrastructure.DepartmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestEndpoint {

    private final DepartmentRepository departmentRepository;

    public TestEndpoint(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @GetMapping
    public Page<?> f1(){
        return departmentRepository.findAllDepartmentsByUserId(PageRequest.of(0,10),1L);
    }
}
