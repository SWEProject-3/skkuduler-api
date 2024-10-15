package com.skku.skkuduler.presentation.endpoint;

import com.skku.skkuduler.application.DepartmentService;
import com.skku.skkuduler.common.security.JwtUtil;
import com.skku.skkuduler.dto.response.DepartmentSearchResponseDto;
import com.skku.skkuduler.presentation.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/departments")
public class DepartmentEndpoint {

    private final DepartmentService departmentService;
    private final JwtUtil jwtUtil;

    public DepartmentEndpoint(DepartmentService departmentService, JwtUtil jwtUtil) {
        this.departmentService = departmentService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ApiResponse<Page<DepartmentSearchResponseDto>> getDepartments(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                         @RequestParam(required = false, name = "query") String query,
                                                                         @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token){
        Long userId = jwtUtil.extractUserId(token);
        Pageable pageable = PageRequest.of(page,10);
        if(query == null || query.isBlank() || query.isEmpty()){
            return new ApiResponse<>(departmentService.getAllDepartmentsByUser(pageable,userId));
        }else {
            return new ApiResponse<>(departmentService.searchDepartmentsByUser(query,pageable, userId));
        }
    }
}
