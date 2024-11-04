package com.skku.skkuduler.application;

import com.skku.skkuduler.dto.response.DepartmentSearchResponseDto;
import com.skku.skkuduler.dto.response.DepartmentSummaryDto;
import com.skku.skkuduler.infrastructure.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Transactional(readOnly = true)
    public Page<DepartmentSearchResponseDto> searchDepartmentsByUser(String query, Pageable pageable, Long userId) {
        return departmentRepository.searchDepartmentsByUserId(query,pageable,userId);
    }

    @Transactional(readOnly = true)
    public Page<DepartmentSearchResponseDto> getAllDepartmentsByUser(Pageable pageable , Long userId) {
        return departmentRepository.findAllDepartmentsByUserId(pageable,userId);
    }

    @Transactional(readOnly = true)
    public Page<DepartmentSummaryDto> getSubscribedDepartments(Long userId, Pageable pageable) {
        return departmentRepository.findDepartmentsSubscribedByUser(userId, pageable);
    }
}