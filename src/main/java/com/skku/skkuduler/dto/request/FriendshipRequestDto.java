package com.skku.skkuduler.dto.request;

public class FriendshipRequestDto {
    private Long fromUserId;
    private Long toUserId;

    // Getters and Setters
    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }
}
