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
public class FriendInfoDto {
    private Long friendshipId;
    private Long friendUserId;
    private String friendName;
    private String friendEmail;
    private FriendshipStatus status;
}
