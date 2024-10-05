package com.skku.skkuduler.presentation.endpoint;

import com.skku.skkuduler.application.AuthService;
import com.skku.skkuduler.dto.request.UserLoginRequest;
import com.skku.skkuduler.dto.request.UserRegistrationRequest;
import com.skku.skkuduler.presentation.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthEndpoint {

    private final AuthService authService;


    @PostMapping("/register")
    public ApiResponse<String> registerUser(@RequestBody UserRegistrationRequest userRequest) {
        authService.registerUser(userRequest);
        return new ApiResponse<>(200, "User registered successfully");
    }

    @PostMapping("/login")
    public ApiResponse<String> loginUser(@RequestBody UserLoginRequest loginRequest) {
        String token = authService.loginUser(loginRequest);
        if (token != null) {
            return new ApiResponse<>(200, "User login successfully", token);
        } else {
            return new ApiResponse<>(401, "User login failed");
        }
    }
}
