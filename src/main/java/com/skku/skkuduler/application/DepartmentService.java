package com.skku.skkuduler.application;

import com.skku.skkuduler.common.exception.Error;
import com.skku.skkuduler.common.exception.ErrorException;
import com.skku.skkuduler.domain.user.Subscription;
import com.skku.skkuduler.domain.user.User;
import com.skku.skkuduler.dto.request.SortType;
import com.skku.skkuduler.dto.response.DepartmentEventSummaryDto;
import com.skku.skkuduler.dto.response.DepartmentSearchResponseDto;
import com.skku.skkuduler.dto.response.DepartmentSummaryDto;
import com.skku.skkuduler.infrastructure.DepartmentRepository;
import com.skku.skkuduler.infrastructure.EventRepository;
import com.skku.skkuduler.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    public Page<DepartmentSearchResponseDto> searchDepartmentsByUser(String query, Pageable pageable, Long userId) {
        return departmentRepository.searchDepartmentsByUserId(query,pageable,userId);
    }

    @Transactional(readOnly = true)
    public Page<DepartmentSearchResponseDto> getAllDepartmentsByUser(Pageable pageable , Long userId) {
        return departmentRepository.findAllDepartmentsByUserId(pageable,userId);
    }

    @Transactional(readOnly = true)
    public Page<DepartmentSummaryDto> getSubscribedDepartments(Long userId, Pageable pageable) {
        return departmentRepository.findDepartmentsSubscribedByUser(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<DepartmentEventSummaryDto> getDepartmentsEvents(Long userId, String query, SortType sortType, Pageable pageable){
        User user = userRepository.findByIdFetchSubscriptions(userId).orElseThrow(() -> new ErrorException(Error.USER_NOT_FOUND));
        List<Subscription> subscription = user.getSubscriptions();
        if(subscription.isEmpty()) throw new ErrorException(Error.SUBSCRIPTIONS_NOT_EXIST);
        List<Long> departmentIds = subscription.stream().map(Subscription::getDepartmentId).toList();
        return eventRepository.getDepartmentEventSummaryDtos(departmentIds,sortType,query, pageable);
    }

}