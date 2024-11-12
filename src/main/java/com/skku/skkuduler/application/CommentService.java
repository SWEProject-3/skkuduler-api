package com.skku.skkuduler.application;

import com.skku.skkuduler.common.exception.Error;
import com.skku.skkuduler.common.exception.ErrorException;
import com.skku.skkuduler.domain.comment.Comment;
import com.skku.skkuduler.dto.response.CommentInfo;
import com.skku.skkuduler.infrastructure.CommentRepository;
import com.skku.skkuduler.infrastructure.EventRepository;
import com.skku.skkuduler.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;


    @Transactional
    public void createComment(Long eventId, Long userId, String content) {
        if(!userRepository.existsByUserId(userId)) throw new ErrorException(Error.USER_NOT_FOUND);
        if(!eventRepository.existsById(eventId)) throw new ErrorException(Error.EVENT_NOT_FOUND);
        Comment comment = Comment.of(eventId,userId);
        comment.changeContent(content);
        commentRepository.save(comment);
    }

    @Transactional
    public void updateComment(Long commentId, Long userId, String content) {
        if(!userRepository.existsByUserId(userId)) throw new ErrorException(Error.USER_NOT_FOUND);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ErrorException(Error.COMMENT_NOT_FOUND));
        if(!comment.getUserId().equals(userId)) throw new ErrorException(Error.PERMISSION_DENIED);
        comment.changeContent(content);
        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        if(!userRepository.existsByUserId(userId)) throw new ErrorException(Error.USER_NOT_FOUND);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ErrorException(Error.COMMENT_NOT_FOUND));
        if(!comment.getUserId().equals(userId)) throw new ErrorException(Error.PERMISSION_DENIED);
        commentRepository.delete(comment);
    }

    public Page<CommentInfo> getComments(Long eventId, Long viewerId, Pageable p) {
        if(!eventRepository.existsById(eventId)) throw new ErrorException(Error.EVENT_NOT_FOUND);
        return commentRepository.findComments(eventId,viewerId,p);
    }
}
