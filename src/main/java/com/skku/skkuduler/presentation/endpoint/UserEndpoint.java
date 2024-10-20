// src/main/java/com/thecodealchemist/controller/UserController.java
package com.skku.skkuduler.presentation.endpoint;

import com.skku.skkuduler.application.DepartmentService;
import com.skku.skkuduler.application.UserService;
import com.skku.skkuduler.common.exception.Error;
import com.skku.skkuduler.common.exception.ErrorException;
import com.skku.skkuduler.common.security.JwtUtil;
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
@RequestMapping("/api/users")
public class UserEndpoint {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final DepartmentService departmentService;

    @GetMapping("/{userId}/subscriptions")
    public ApiResponse<Page<DepartmentSummaryDto>> getSubscribedDepartments(@PathVariable("userId") Long userId,
                                                                            @RequestParam(value = "page", defaultValue = "0") int page,
                                                                            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        // 친구가 아니거나 내 목록을 조회하지 않을시 exception 발생 로직 추가
        Pageable pageable = PageRequest.of(page,10);
        return new ApiResponse<>(departmentService.getSubscribedDepartments(userId,pageable));
    }
    @PostMapping("/{userId}/subscriptions/{departmentId}")
    public ApiResponse<Void> subscribeDepartment(@PathVariable("userId") Long userId,
                                                 @PathVariable("departmentId") Long departmentId,
                                                 @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        if (!userId.equals(jwtUtil.extractUserId(token))) throw new ErrorException(Error.PERMISSION_DENIED);
        userService.subscribeDepartment(userId, departmentId);
        return new ApiResponse<>("Subscribed successfully");
    }

    @DeleteMapping("/{userId}/subscriptions/{departmentId}")
    public ApiResponse<Void> unsubscribeDepartment(@PathVariable("userId") Long userId,
                                                   @PathVariable("departmentId") Long departmentId,
                                                   @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        if (!userId.equals(jwtUtil.extractUserId(token))) throw new ErrorException(Error.PERMISSION_DENIED);
        userService.unsubscribeDepartment(userId, departmentId);
        return new ApiResponse<>("Unsubscribed successfully");
    }
}