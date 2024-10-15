package com.skku.skkuduler.application;

import com.skku.skkuduler.common.exception.UserNotFoundException;
import com.skku.skkuduler.domain.user.User;
import com.skku.skkuduler.dto.response.DepartmentSummaryDto;
import com.skku.skkuduler.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void subscribeDepartment(Long userId, Long departmentId){
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.subscribeDepartment(departmentId);
        userRepository.save(user);
    }

    @Transactional
    public void unsubscribeDepartment(Long userId, Long departmentId){
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.unsubscribeDepartment(departmentId);
        userRepository.save(user);
    }

}
