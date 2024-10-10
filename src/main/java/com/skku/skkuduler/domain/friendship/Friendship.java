package com.skku.skkuduler.domain.friendship;

import com.skku.skkuduler.domain.BaseEntity;
import com.skku.skkuduler.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
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