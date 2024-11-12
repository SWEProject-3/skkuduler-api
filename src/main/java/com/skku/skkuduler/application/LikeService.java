package com.skku.skkuduler.application;

import com.skku.skkuduler.common.exception.Error;
import com.skku.skkuduler.common.exception.ErrorException;
import com.skku.skkuduler.domain.like.Likes;
import com.skku.skkuduler.infrastructure.EventRepository;
import com.skku.skkuduler.infrastructure.LikesRepository;
import com.skku.skkuduler.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Transactional
    public void likeEvent(Long eventId, Long userId) {
        if(!userRepository.existsByUserId(userId)) throw new ErrorException(Error.USER_NOT_FOUND);
        if(!eventRepository.existsById(eventId)) throw new ErrorException(Error.EVENT_NOT_FOUND);
        Likes like = Likes.of(userId, eventId);
        try {
            likesRepository.save(like);
        }catch(DataIntegrityViolationException e){
            throw new ErrorException(Error.ALREADY_LIKE);
        }
    }

    @Transactional
    public void unlikeEvent(Long eventId, Long userId){
        Likes like = likesRepository.findByUserIdAndEventId(userId,eventId).orElseThrow(()-> new ErrorException(Error.INVALID_UNLIKE));
        likesRepository.delete(like);
    }
}
