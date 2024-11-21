package com.skku.skkuduler.application;

import com.skku.skkuduler.common.exception.Error;
import com.skku.skkuduler.common.exception.ErrorException;
import com.skku.skkuduler.domain.calender.Event;
import com.skku.skkuduler.domain.user.User;
import com.skku.skkuduler.infrastructure.EventRepository;
import com.skku.skkuduler.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    public boolean hasDepartmentPermission(Long userId, Long departmentId) {
        User user = userRepository.findByIdFetchPermissions(userId).orElseThrow(()-> new ErrorException(Error.USER_NOT_FOUND));
        return user.hasPermission(departmentId);
    }

    @Transactional(readOnly = true)
    public boolean hasEventPermission(Long userId, Long eventId){
        User user = userRepository.findByIdFetchPermissions(userId).orElseThrow(()-> new ErrorException(Error.USER_NOT_FOUND));
        Event event = eventRepository.findById(eventId).orElseThrow(()-> new ErrorException(Error.EVENT_NOT_FOUND));
        return user.hasPermission(event.getDepartmentId());
    }
}
