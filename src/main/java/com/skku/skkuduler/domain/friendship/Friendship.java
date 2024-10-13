package com.skku.skkuduler.domain.friendship;

import com.skku.skkuduler.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "friendship")
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"from_user_id", "to_user_id"})
)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Friendship extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendshipId;

    private Long fromUserId;

    private Long toUserId;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

    enum FriendshipStatus {
        PENDING,
        ACCEPTED,
        REJECTED
    }
}