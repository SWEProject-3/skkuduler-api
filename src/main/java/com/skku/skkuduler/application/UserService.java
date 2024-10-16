package com.skku.skkuduler.application;

import com.skku.skkuduler.common.exception.DepartmentNotFoundException;
import com.skku.skkuduler.common.exception.DuplicatedException;
import com.skku.skkuduler.common.exception.SubscriptionNotFoundException;
import com.skku.skkuduler.common.exception.UserNotFoundException;
import com.skku.skkuduler.domain.department.Department;
import com.skku.skkuduler.domain.user.User;
import com.skku.skkuduler.infrastructure.DepartmentRepository;
import com.skku.skkuduler.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public void subscribeDepartment(Long userId, Long departmentId){
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if(!departmentRepository.existsById(departmentId)) throw new DepartmentNotFoundException();
        user.subscribeDepartment(departmentId);
        try {
            userRepository.save(user);
        }catch (DataIntegrityViolationException e){
            throw new DuplicatedException("duplicated subscription is not allowed");
        }
    }

    @Transactional
    public void unsubscribeDepartment(Long userId, Long departmentId){
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if(!departmentRepository.existsById(departmentId)) throw new DepartmentNotFoundException();
        if(!user.unsubscribeDepartment(departmentId)) throw new SubscriptionNotFoundException();
        userRepository.save(user);
    }

}
