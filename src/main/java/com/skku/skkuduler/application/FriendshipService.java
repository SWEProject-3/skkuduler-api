package com.skku.skkuduler.application;

import com.skku.skkuduler.domain.user.User;
import com.skku.skkuduler.domain.friendship.Friendship;
import com.skku.skkuduler.domain.friendship.Friendship.FriendshipStatus;
import com.skku.skkuduler.dto.response.FriendshipResponseDto;
import com.skku.skkuduler.infrastructure.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.skku.skkuduler.common.exception.Error;
import com.skku.skkuduler.common.exception.ErrorException;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class FriendshipService {


    private final FriendshipRepository friendshipRepository;

    @Transactional
    public FriendshipResponseDto sendFriendRequest(User fromUser, User toUser) {
        boolean isAlreadyFriends = friendshipRepository.existsFriendshipByUserIds(fromUser.getUserId(), toUser.getUserId());
        if (isAlreadyFriends) {
            throw new ErrorException(Error.ALREADY_FRIEND);
        }

        Friendship friendship = new Friendship();
        friendship.setFromUserId(fromUser.getUserId());
        friendship.setToUserId(toUser.getUserId());
        friendship.setStatus(FriendshipStatus.PENDING);

        Friendship savedFriendship = friendshipRepository.save(friendship);

        return FriendshipResponseDto.builder()
                .friendshipId(savedFriendship.getFriendshipId())
                .fromUserId(savedFriendship.getFromUserId())
                .toUserId(savedFriendship.getToUserId())
                .status(savedFriendship.getStatus())
                .build();
    }

    @Transactional(readOnly = true)
    public List<FriendshipResponseDto> getFriendRequestsByStatus(Long userId, FriendshipStatus status) {
        List<Friendship> friendships = friendshipRepository.findByUserIdAndStatus(userId, status);

        return friendships.stream()
                .map(friendship -> FriendshipResponseDto.builder()
                        .friendshipId(friendship.getFriendshipId())
                        .fromUserId(friendship.getFromUserId())
                        .toUserId(friendship.getToUserId())
                        .status(friendship.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public FriendshipResponseDto acceptFriendRequest(Long friendshipId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new ErrorException(Error.NOT_FOUND_FRIENDSHIP));

        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new ErrorException(Error.NOT_PENDING_FRIENDSHIP);
        }

        friendship.setStatus(FriendshipStatus.ACCEPTED);
        Friendship updatedFriendship = friendshipRepository.save(friendship);

        return FriendshipResponseDto.builder()
                .friendshipId(updatedFriendship.getFriendshipId())
                .fromUserId(updatedFriendship.getFromUserId())
                .toUserId(updatedFriendship.getToUserId())
                .status(updatedFriendship.getStatus())
                .build();
    }

    @Transactional
    public FriendshipResponseDto rejectFriendRequest(Long friendshipId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new ErrorException(Error.NOT_FOUND_FRIENDSHIP));

        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new ErrorException(Error.NOT_PENDING_FRIENDSHIP);
        }

        friendship.setStatus(FriendshipStatus.REJECTED);
        Friendship updatedFriendship = friendshipRepository.save(friendship);

        return FriendshipResponseDto.builder()
                .friendshipId(updatedFriendship.getFriendshipId())
                .fromUserId(updatedFriendship.getFromUserId())
                .toUserId(updatedFriendship.getToUserId())
                .status(updatedFriendship.getStatus())
                .build();
    }

    public FriendshipResponseDto findFriendshipById(Long friendshipId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new ErrorException(Error.NOT_PENDING_FRIENDSHIP));

        return FriendshipResponseDto.builder()
                .friendshipId(friendship.getFriendshipId())
                .fromUserId(friendship.getFromUserId())
                .toUserId(friendship.getToUserId())
                .status(friendship.getStatus())
                .build();
    }


    //userId가 보낸 모든 친구요청 조회
    @Transactional(readOnly = true)
    public List<FriendshipResponseDto> getFriendRequestsSentByUser(Long fromUserId) {
        List<Friendship> friendships = friendshipRepository.findByFromUserId(fromUserId);

        return friendships.stream()
                .map(friendship -> FriendshipResponseDto.builder()
                        .friendshipId(friendship.getFriendshipId())
                        .fromUserId(friendship.getFromUserId())
                        .toUserId(friendship.getToUserId())
                        .status(friendship.getStatus())
                        .build())
                .collect(Collectors.toList());
    }


    //userId가 받은 모든 친구요청 조회
    @Transactional(readOnly = true)
    public List<FriendshipResponseDto> getFriendRequestsReceivedByUser(Long toUserId) {
        List<Friendship> friendships = friendshipRepository.findByToUserId(toUserId);

        return friendships.stream()
                .map(friendship -> FriendshipResponseDto.builder()
                        .friendshipId(friendship.getFriendshipId())
                        .fromUserId(friendship.getFromUserId())
                        .toUserId(friendship.getToUserId())
                        .status(friendship.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelPendingFriendRequest(Long friendshipId, Long fromUserId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new ErrorException(Error.NOT_FOUND_FRIENDSHIP));

        // 친구 요청이 PENDING 상태인지, fromUserId가 일치하는지 확인
        if (!friendship.getStatus().equals(FriendshipStatus.PENDING)) {
            throw new ErrorException(Error.NOT_PENDING_FRIENDSHIP);
        }
        if (!friendship.getFromUserId().equals(fromUserId)) {
            throw new ErrorException(Error.PERMISSION_DENIED);
        }

        friendshipRepository.delete(friendship);
    }
}
