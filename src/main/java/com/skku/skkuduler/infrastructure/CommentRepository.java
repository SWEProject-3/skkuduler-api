package com.skku.skkuduler.infrastructure;

import com.skku.skkuduler.domain.comment.Comment;
import com.skku.skkuduler.dto.response.CommentInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query("""
    SELECT distinct new com.skku.skkuduler.dto.response.CommentInfo(
        c.commentId,
        CASE WHEN c.userId = :viewerId THEN true ELSE false END,
        c.userId,
        u.name,
        c.createdAt,
        CASE WHEN c.lastModifiedAt = c.createdAt THEN false ELSE true END,
        c.content
    )
    FROM comment c
    LEFT JOIN user u ON c.userId = u.userId
    WHERE c.eventId = :eventId
    """)
    Page<CommentInfo> findComments(@Param("eventId") Long eventId, @Param("viewerId") Long viewerId, Pageable p);
}
