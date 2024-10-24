package com.skku.skkuduler.application;

import com.skku.skkuduler.common.exception.Error;
import com.skku.skkuduler.common.exception.ErrorException;
import com.skku.skkuduler.domain.calender.Calender;
import com.skku.skkuduler.domain.user.User;
import com.skku.skkuduler.dto.response.CalenderInfoDto;
import com.skku.skkuduler.infrastructure.CalenderRepository;
import com.skku.skkuduler.infrastructure.FriendshipRepository;
import com.skku.skkuduler.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalenderService {

    private final CalenderRepository calenderRepository;
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    @Transactional(readOnly = true)
    public List<CalenderInfoDto> loadUserCalenders(Long userId, Long viewerId) {
        if (!userId.equals(viewerId) && !friendshipRepository.existsFriendshipByUserIds(userId, viewerId)) {
            throw new ErrorException(Error.NOT_FRIEND);
        }
        return calenderRepository.findAllUserCalendersToCalenderInfoDto(userId);
    }

    @Transactional(readOnly = true)
    public List<CalenderInfoDto> loadDepartmentCalenders(Long departmentId, Long viewerId) {
        User viewer = userRepository.findById(viewerId).orElseThrow(() -> new ErrorException(Error.USER_NOT_FOUND));
        if (!viewer.isSubscribed(departmentId)) throw new ErrorException(Error.NOT_SUBSCRIBED);
        return calenderRepository.findAllDeptCalendersToCalenderInfoDto(departmentId);
    }

    @Transactional
    public void createUserCalender(Long userId, String name) {
        Calender calender = Calender.userCalender(userId);
        calender.changeName(name);
        calenderRepository.save(calender);
    }

    @Transactional
    public void updateUserCalender(Long calenderId, Long userId, String name) {
        Calender calender = calenderRepository.findById(calenderId).orElseThrow(() -> new ErrorException(Error.CALENDER_NOT_FOUND));
        if (!calender.getUserId().equals(userId)) throw new ErrorException(Error.PERMISSION_DENIED);
        calender.changeName(name);
        calenderRepository.save(calender);
    }

    @Transactional
    public void deleteUserCalender(Long calenderId, Long userId) {
        Calender calender = calenderRepository.findById(calenderId).orElseThrow(() -> new ErrorException(Error.CALENDER_NOT_FOUND));
        if (!calender.getUserId().equals(userId)) throw new ErrorException(Error.PERMISSION_DENIED);
        calenderRepository.delete(calender);
    }
}
