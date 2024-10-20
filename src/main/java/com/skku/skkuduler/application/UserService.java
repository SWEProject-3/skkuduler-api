package com.skku.skkuduler.application;

import com.skku.skkuduler.common.exception.Error;
import com.skku.skkuduler.common.exception.ErrorException;
import com.skku.skkuduler.domain.user.User;
import com.skku.skkuduler.infrastructure.DepartmentRepository;
import com.skku.skkuduler.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
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
        User user = userRepository.findById(userId).orElseThrow(() -> new ErrorException(Error.USER_NOT_FOUND));
        if(!departmentRepository.existsById(departmentId)) throw new ErrorException(Error.DEPARTMENT_NOT_FOUND);
        user.subscribeDepartment(departmentId);
        try {
            userRepository.save(user);
        }catch (DataIntegrityViolationException e){
            throw new ErrorException(Error.ALREADY_SUBSCRIBED);
        }
    }

    @Transactional
    public void unsubscribeDepartment(Long userId, Long departmentId){
        User user = userRepository.findById(userId).orElseThrow(() -> new ErrorException(Error.USER_NOT_FOUND));
        if(!departmentRepository.existsById(departmentId)) throw new ErrorException(Error.DEPARTMENT_NOT_FOUND);
        if(!user.unsubscribeDepartment(departmentId)) throw new ErrorException(Error.INVALID_UNSUBSCRIPTION);
        userRepository.save(user);
    }

}
