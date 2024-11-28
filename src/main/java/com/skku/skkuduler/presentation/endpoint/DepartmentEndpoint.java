package com.skku.skkuduler.presentation.endpoint;

import com.skku.skkuduler.application.DepartmentService;
import com.skku.skkuduler.common.exception.Error;
import com.skku.skkuduler.common.exception.ErrorException;
import com.skku.skkuduler.common.security.JwtUtil;
import com.skku.skkuduler.dto.request.SortType;
import com.skku.skkuduler.dto.response.DepartmentEventSummaryDto;
import com.skku.skkuduler.dto.response.DepartmentSearchResponseDto;
import com.skku.skkuduler.dto.response.DepartmentSummaryDto;
import com.skku.skkuduler.presentation.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DepartmentEndpoint {

    private final DepartmentService departmentService;
    private final JwtUtil jwtUtil;

    @GetMapping("/users/{userId}/subscriptions/departments")
    public ApiResponse<Page<DepartmentSummaryDto>> getSubscribedDepartments(@PathVariable("userId") Long userId,
                                                                            @RequestParam(value = "page", required = false) Integer page,
                                                                            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        Pageable pageable = page == null ? Pageable.unpaged() : PageRequest.of(page, 10);
        return new ApiResponse<>(departmentService.getSubscribedDepartments(userId, pageable));
    }

    @GetMapping("/departments")
    public ApiResponse<Page<DepartmentSearchResponseDto>> getDepartments(@RequestParam(name = "page", required = false) Integer page,
                                                                         @RequestParam(required = false, name = "query") String query,
                                                                         @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token){
        Long userId = jwtUtil.extractUserId(token);
        Pageable pageable = page == null ? Pageable.unpaged() : PageRequest.of(page,10);
        if(query == null || query.isBlank() || query.isEmpty()){
            return new ApiResponse<>(departmentService.getAllDepartmentsByUser(pageable,userId));
        }else {
            return new ApiResponse<>(departmentService.searchDepartmentsByUser(query,pageable, userId));
        }
    }
    @GetMapping("/users/subscriptions/departments/events")
    public ApiResponse<Page<DepartmentEventSummaryDto>> getSubscribedDepartmentsEvent(@RequestParam(value = "sort",defaultValue = "latest") String sort,
                                                                                      @RequestParam(value = "query",required = false) String query,
                                                                                      @RequestParam(value = "page", required = false) Integer page,
                                                                                      @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        Long userId = jwtUtil.extractUserId(token);
        Pageable p = page == null ? Pageable.unpaged() : PageRequest.of(page,10);
        SortType sortType = SortType.of(sort).orElseThrow(()-> new ErrorException(Error.INVALID_URL_PARAMETERS));
        System.out.println("sort = " + sort);
        System.out.println("sortType = " + sortType);
        return new ApiResponse<>(departmentService.getDepartmentsEvents(userId, query,sortType,p));

    }

}
