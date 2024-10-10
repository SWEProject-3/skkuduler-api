package com.skku.skkuduler.domain.user;

import com.skku.skkuduler.domain.BaseEntity;
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

    @ManyToOne
    @JoinColumn(name = "myId")
    private User myId;

    @ManyToOne
    @JoinColumn(name = "friendId")
    private User friendId;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

    enum FriendshipStatus {
        PENDING,
        ACCEPTED,
        REJECTED
    }
}