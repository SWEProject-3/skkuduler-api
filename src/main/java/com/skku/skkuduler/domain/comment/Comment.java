package com.skku.skkuduler.domain.comment;

import com.skku.skkuduler.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "comment")
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Long eventId;

    @Column(nullable = false)
    private Long userId;

    public static Comment of(Long eventId, Long userId){
        return new Comment(null, null, eventId,userId);
    }

    public void changeContent(String content){
        if(content != null) this.content = content;
    }

}
