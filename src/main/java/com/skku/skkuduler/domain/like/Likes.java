package com.skku.skkuduler.domain.like;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","event_id"})
)
@AllArgsConstructor
@NoArgsConstructor
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long eventId;

    public static Likes of(Long userId, Long eventId){
        return new Likes(null, userId,eventId);
    }
}
