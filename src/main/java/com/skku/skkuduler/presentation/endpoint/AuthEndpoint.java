package com.skku.skkuduler.presentation.endpoint;

import com.skku.skkuduler.application.AccountService;
import com.skku.skkuduler.application.AuthService;
import com.skku.skkuduler.common.exception.Error;
import com.skku.skkuduler.common.exception.ErrorException;
import com.skku.skkuduler.dto.request.PasswordChangeDto;
import com.skku.skkuduler.dto.request.UserLoginRequestDto;
import com.skku.skkuduler.dto.request.UserRegistrationRequestDto;
import com.skku.skkuduler.dto.response.LoginSuccessDto;
import com.skku.skkuduler.dto.response.UserInfoDto;
import com.skku.skkuduler.presentation.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthEndpoint {

    private final AuthService authService;
    private final AccountService accountService;
    @PostMapping("/register")
    public ApiResponse<String> registerUser(@RequestBody @Valid UserRegistrationRequestDto userRequest) {
        accountService.registerUser(userRequest);
        return new ApiResponse<>(200, "User registered successfully");
    }

    @PostMapping("/login")
    public ApiResponse<LoginSuccessDto> loginUser(@RequestBody UserLoginRequestDto loginRequest) {
        return new ApiResponse<>(accountService.loginUser(loginRequest));
    }

    @GetMapping("/check-token")
    public ApiResponse<UserInfoDto> checkToken(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        return new ApiResponse<>(authService.checkToken(token));
    }

    @PutMapping("/password")
    public ApiResponse<Void> changePassword(@RequestBody @Valid PasswordChangeDto passwordChangeDto,
                                            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        accountService.changePassword(token, passwordChangeDto.getOldPassword(),passwordChangeDto.getNewPassword());
        return new ApiResponse<>("Password Changed Successful");
    }

}