package com.skku.skkuduler.application;

import com.skku.skkuduler.common.exception.Error;
import com.skku.skkuduler.common.exception.ErrorException;
import com.skku.skkuduler.common.security.JwtUtil;
import com.skku.skkuduler.domain.calender.Calendar;
import com.skku.skkuduler.domain.user.User;
import com.skku.skkuduler.dto.request.UserLoginRequestDto;
import com.skku.skkuduler.dto.request.UserRegistrationRequestDto;
import com.skku.skkuduler.dto.response.LoginSuccessDto;
import com.skku.skkuduler.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public void registerUser(UserRegistrationRequestDto userRequest) {
        User user = new User();
        user.changeEmail(userRequest.getEmail());
        user.changeName(userRequest.getName());
        user.changePassword(passwordEncoder.encode(userRequest.getPassword()));
        user.changeRole(User.Role.USER);
        Calendar calendar = Calendar.of(user);
        user.changeCalender(calendar);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public LoginSuccessDto loginUser(UserLoginRequestDto loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new ErrorException(Error.USER_NOT_FOUND));
        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return new LoginSuccessDto(jwtUtil.generateToken(user.getUserId(), user.getRole()),user.getUserId());
        }
        throw new ErrorException(Error.LOGIN_FAILED);
    }

    @Transactional
    public void changePassword(String token, String oldPassword, String newPassword){
        User user = userRepository.findById(jwtUtil.extractUserId(token)).orElseThrow(() -> new ErrorException(Error.USER_NOT_FOUND));
        if(!passwordEncoder.matches(oldPassword, user.getPassword())) throw new ErrorException(Error.PERMISSION_DENIED);
        user.changePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
