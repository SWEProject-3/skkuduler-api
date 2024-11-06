package com.skku.skkuduler.domain.friendship;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity(name = "friendship")
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"from_user_id", "to_user_id"})
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Friendship{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendshipId;

    private Long fromUserId;

    private Long toUserId;

    public enum FriendshipStatus {
        PENDING,   // 친구 요청 대기 중
        ACCEPTED,  // 친구 요청 수락
    }

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;


}