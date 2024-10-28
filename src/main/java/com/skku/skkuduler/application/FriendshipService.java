package com.skku.skkuduler.application;

import com.skku.skkuduler.domain.user.User;
import com.skku.skkuduler.domain.friendship.Friendship;
import com.skku.skkuduler.domain.friendship.FriendshipStatus;
import com.skku.skkuduler.dto.request.FriendshipRequestDto;
import com.skku.skkuduler.infrastructure.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class FriendshipService {


    private final FriendshipRepository friendshipRepository;

    @Transactional
    public Friendship sendFriendRequest(User fromUser, User toUser) {
        // 기존에 ACCEPTED 상태의 친구 관계가 있는지 확인
        boolean isAlreadyFriends = friendshipRepository.existsFriendshipByUserIds(fromUser.getUserId(), toUser.getUserId());
        if (isAlreadyFriends) {
            throw new IllegalArgumentException("You are already friends.");
        }

        // 새로운 친구 요청 생성
        Friendship friendship = new Friendship();
        friendship.setFromUserId(fromUser.getUserId());
        friendship.setToUserId(toUser.getUserId());
        friendship.setStatus(FriendshipStatus.PENDING);

        // 저장
        return friendshipRepository.save(friendship);
    }

    @Transactional(readOnly = true)
    public List<Friendship> getFriendRequestsByStatus(Long userId, FriendshipStatus status) {
        return friendshipRepository.findByUserIdAndStatus(userId, status);
    }

    @Transactional(readOnly = true)
    public List<Friendship> getSentFriendRequestsByStatus(Long userId, FriendshipStatus status) {
        return friendshipRepository.findBySentUserIdAndStatus(userId, status);
    }

    @Transactional(readOnly = true)
    public List<Friendship> getReceivedFriendRequestsByStatus(Long userId, FriendshipStatus status) {
        return friendshipRepository.findByReceivedUserIdAndStatus(userId, status);
    }

    @Transactional
    public Friendship acceptFriendRequest(Long friendshipId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new IllegalArgumentException("Friend request not found"));

        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new IllegalStateException("Friend request is not pending");
        }

        friendship.setStatus(FriendshipStatus.ACCEPTED);
        return friendshipRepository.save(friendship);
    }

    @Transactional
    public Friendship rejectFriendRequest(Long friendshipId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new IllegalArgumentException("Friend request not found"));

        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new IllegalStateException("Friend request is not pending");
        }

        friendship.setStatus(FriendshipStatus.REJECTED);
        return friendshipRepository.save(friendship);
    }

    public Friendship findFriendshipById(Long friendshipId) {
        return friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new IllegalArgumentException("Friendship request not found"));
    }


    // userId와 관련된 모든 친구요청 조회 (맨 아래와 중복)
    @Transactional(readOnly = true)
    public List<Friendship> getAllFriendRequests(Long userId) {
        return friendshipRepository.findAllByUserId(userId);
    }

    //userId가 보낸 모든 친구요청 조회
    @Transactional(readOnly = true)
    public List<Friendship> getFriendRequestsSentByUser(Long fromUserId) {
        return friendshipRepository.findByFromUserId(fromUserId);
    }

    //userId가 받은 모든 친구요청 조회
    @Transactional(readOnly = true)
    public List<Friendship> getFriendRequestsReceivedByUser(Long toUserId) {
        return friendshipRepository.findByToUserId(toUserId);
    }

    // userId와 관련된 모든 친구요청 조회
    @Transactional(readOnly = true)
    public List<Friendship> getFriendRequestsAllByUser(Long userId) {
        return friendshipRepository.findAllByUserId(userId);
    }

    @Transactional(readOnly=true)
    public List<Friendship> getAllFriendsByUser(Long userId) {
        return friendshipRepository.findFriendsByUserId(userId);
    }

    @Transactional
    public void cancelPendingFriendRequest(Long friendshipId, Long fromUserId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new IllegalArgumentException("Friend request not found"));

        // 친구 요청이 PENDING 상태인지, fromUserId가 일치하는지 확인
        if (!friendship.getStatus().equals(FriendshipStatus.PENDING)) {
            throw new IllegalStateException("Friend request is not in PENDING status.");
        }
        if (!friendship.getFromUserId().equals(fromUserId)) {
            throw new IllegalStateException("You are not authorized to cancel this friend request.");
        }

        friendshipRepository.delete(friendship);
    }
}
