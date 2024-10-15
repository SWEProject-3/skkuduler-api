package com.skku.skkuduler.application;

import com.skku.skkuduler.dto.response.DepartmentSearchResponseDto;
import com.skku.skkuduler.infrastructure.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public Page<DepartmentSearchResponseDto> searchDepartmentsByUser(String query, Pageable pageable, Long userId) {
        return departmentRepository.searchDepartmentsByUserId(query,pageable,userId);
    }

    public Page<DepartmentSearchResponseDto> getAllDepartmentsByUser(Pageable pageable , Long userId) {
        return departmentRepository.findAllDepartmentsByUserId(pageable,userId);
    }
}
