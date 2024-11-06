// src/main/java/com/thecodealchemist/controller/UserController.java
package com.skku.skkuduler.presentation.endpoint;

import com.skku.skkuduler.application.DepartmentService;
import com.skku.skkuduler.application.UserService;
import com.skku.skkuduler.common.exception.Error;
import com.skku.skkuduler.common.exception.ErrorException;
import com.skku.skkuduler.common.security.JwtUtil;
import com.skku.skkuduler.dto.request.ProfileUpdateDto;
import com.skku.skkuduler.dto.request.UserPasswordDto;
import com.skku.skkuduler.dto.response.DepartmentSummaryDto;
import com.skku.skkuduler.presentation.ApiResponse;
import jakarta.validation.Valid;
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
        Pageable pageable = PageRequest.of(page, 10);
        return new ApiResponse<>(departmentService.getSubscribedDepartments(userId, pageable));
    }

    @PostMapping("/subscriptions/{departmentId}")
    public ApiResponse<Void> subscribeDepartment(@PathVariable("departmentId") Long departmentId,
                                                 @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtUtil.extractUserId(token);
        userService.subscribeDepartment(userId, departmentId);
        return new ApiResponse<>("Subscribed successfully");
    }

    @DeleteMapping("/subscriptions/{departmentId}")
    public ApiResponse<Void> unsubscribeDepartment(@PathVariable("departmentId") Long departmentId,
                                                   @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtUtil.extractUserId(token);
        userService.unsubscribeDepartment(userId, departmentId);
        return new ApiResponse<>("Unsubscribed successfully");
    }

    @PutMapping("/profiles")
    public ApiResponse<Void> changeUserProfile(@RequestBody @Valid ProfileUpdateDto profileUpdateDto,
                                               @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtUtil.extractUserId(token);
        userService.changeProfile(userId, profileUpdateDto);
        return new ApiResponse<>("프로필 수정이 성공적으로 완료되었습니다.");
    }

    @DeleteMapping("/withdraw")
    public ApiResponse<Void> withdrawUser(@RequestBody UserPasswordDto userPasswordDto,
                                          @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtUtil.extractUserId(token);
        userService.withdraw(userId, userPasswordDto.getPassword());
        return new ApiResponse<>("회원 탈퇴가 성공적으로 완료되었습니다.");
    }
}