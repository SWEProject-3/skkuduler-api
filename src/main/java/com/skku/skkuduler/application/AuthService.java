package com.skku.skkuduler.application;

import com.skku.skkuduler.common.exception.Error;
import com.skku.skkuduler.common.exception.ErrorException;
import com.skku.skkuduler.common.security.JwtUtil;
import com.skku.skkuduler.domain.calender.Event;
import com.skku.skkuduler.domain.user.User;
import com.skku.skkuduler.dto.response.UserInfoDto;
import com.skku.skkuduler.infrastructure.CalenderRepository;
import com.skku.skkuduler.infrastructure.EventRepository;
import com.skku.skkuduler.infrastructure.FriendshipRepository;
import com.skku.skkuduler.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final EventRepository eventRepository;


    @Transactional(readOnly = true)
    public UserInfoDto checkToken(String token){
        Long userId = jwtUtil.extractUserId(token);
        User user = userRepository.findById(userId).orElseThrow(() -> new ErrorException(Error.USER_NOT_FOUND));
        return UserInfoDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }

    //TODO : event가 isUserEvent이 아닐때 -> admin과 permission
    public void checkAuthForWritingEvent(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ErrorException(Error.EVENT_NOT_FOUND));
        if(event.getIsUserEvent()){
            if(!event.getUserId().equals(userId)){
                throw new ErrorException(Error.PERMISSION_DENIED);
            }
        }else{ // role이 admin이어야 하고, 해당 department에 대한 permission도 있어야한다.

        }
    }


}
