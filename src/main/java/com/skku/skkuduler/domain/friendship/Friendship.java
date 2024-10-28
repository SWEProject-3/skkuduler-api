package com.skku.skkuduler.domain.friendship;

import com.skku.skkuduler.domain.BaseEntity;
import com.skku.skkuduler.domain.friendship.FriendshipStatus;
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

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

}