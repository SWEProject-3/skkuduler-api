package com.skku.skkuduler.dto.response;

import com.skku.skkuduler.domain.friendship.Friendship.FriendshipStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendshipResponseDto {
    private Long friendshipId;
    private Long fromUserId;
    private Long toUserId;
    private FriendshipStatus status;
}
