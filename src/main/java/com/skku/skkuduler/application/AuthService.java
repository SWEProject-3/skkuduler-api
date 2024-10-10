package com.skku.skkuduler.application;

import com.skku.skkuduler.common.exception.UserNotFoundException;
import com.skku.skkuduler.common.security.JwtUtil;
import com.skku.skkuduler.domain.user.User;
import com.skku.skkuduler.dto.request.UserLoginRequestDto;
import com.skku.skkuduler.dto.request.UserRegistrationRequestDto;
import com.skku.skkuduler.infrastructure.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public void registerUser(@Valid UserRegistrationRequestDto userRequest) {
        User user = new User();
        user.changeProfile(userRequest.getEmail(), userRequest.getEmail());
        user.changePassword(passwordEncoder.encode(userRequest.getPassword()));
        user.changeRole(User.Role.USER);
        userRepository.save(user);
    }
    @Transactional(readOnly = true)
    public String loginUser(UserLoginRequestDto loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(UserNotFoundException::new);
        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return jwtUtil.generateToken(user.getEmail(), String.valueOf(user.getRole()));
        }
        return null;
    }
}
