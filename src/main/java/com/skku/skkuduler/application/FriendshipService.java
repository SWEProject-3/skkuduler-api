package com.skku.skkuduler.application;

import com.skku.skkuduler.common.exception.Error;
import com.skku.skkuduler.common.exception.ErrorException;
import com.skku.skkuduler.domain.friendship.Friendship;
import com.skku.skkuduler.domain.friendship.Friendship.FriendshipStatus;
import com.skku.skkuduler.dto.response.FriendInfoDto;
import com.skku.skkuduler.infrastructure.FriendshipRepository;
import com.skku.skkuduler.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
public class FriendshipService {


    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    @Transactional
    public void sendFriendRequest(Long fromUserId, String email) {
        Long toUserId = userRepository.findByEmail(email).orElseThrow(()-> new ErrorException(Error.USER_NOT_FOUND)).getUserId();
        if (fromUserId.equals(toUserId)) throw new ErrorException(Error.INVALID_FRIEND_REQUEST);
        Friendship friendship = friendshipRepository.findFriendshipByUserIds(fromUserId, toUserId).orElse(null);
        if (friendship == null) {
            boolean isUserExists = userRepository.existsByUserId(toUserId) && userRepository.existsByUserId(fromUserId);
            if (!isUserExists) throw new ErrorException(Error.USER_NOT_FOUND);
            friendship = new Friendship();
            friendship.setFromUserId(fromUserId);
            friendship.setToUserId(toUserId);
            friendship.setStatus(FriendshipStatus.PENDING);
        } else { // 이미 상대방이 나에게 친구요청을 보냈을때 ACCEPTED 로 상태 변경
            if (friendship.getStatus() == FriendshipStatus.ACCEPTED) throw new ErrorException(Error.ALREADY_FRIEND);
            if (friendship.getFromUserId().equals(fromUserId) && friendship.getToUserId().equals(toUserId))
                throw new ErrorException(Error.FRIEND_REQUEST_ALREADY_SENT);
            friendship.setStatus(FriendshipStatus.ACCEPTED);
        }
        friendshipRepository.save(friendship);

    }

    @Transactional(readOnly = true)
    public List<FriendInfoDto> getFriends(Long userId) {
        List<FriendInfoDto> response =friendshipRepository.getFriendInfoDtosByUserIdAndAccepted(userId);
        response.sort(Comparator.comparing(FriendInfoDto::getFriendName));
        return response;
    }

    @Transactional
    public void acceptFriendRequest(Long friendshipId, Long toUserId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new ErrorException(Error.FRIEND_REQUEST_NOT_FOUND));

        if (friendship.getStatus() != FriendshipStatus.PENDING || !friendship.getToUserId().equals(toUserId)) {
            throw new ErrorException(Error.FRIEND_REQUEST_NOT_FOUND);
        }
        friendship.setStatus(FriendshipStatus.ACCEPTED);
        friendshipRepository.save(friendship);
    }

    @Transactional
    public void rejectFriendRequest(Long friendshipId, Long toUserId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new ErrorException(Error.FRIEND_REQUEST_NOT_FOUND));

        if (friendship.getStatus() != FriendshipStatus.PENDING || !friendship.getToUserId().equals(toUserId)) {
            throw new ErrorException(Error.FRIEND_REQUEST_NOT_FOUND);
        }
        friendshipRepository.delete(friendship);
    }

    @Transactional
    public void cancelPendingFriendRequest(Long friendshipId, Long fromUserId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new ErrorException(Error.FRIEND_REQUEST_NOT_FOUND));

        if (!friendship.getStatus().equals(FriendshipStatus.PENDING) || !friendship.getFromUserId().equals(fromUserId)) {
            throw new ErrorException(Error.FRIEND_REQUEST_NOT_FOUND);
        }

        friendshipRepository.delete(friendship);
    }

    //userId가 보낸 모든 친구요청 조회
    @Transactional(readOnly = true)
    public List<FriendInfoDto> getFriendRequestsSentByUser(Long fromUserId) {
        return friendshipRepository.getFriendInfoDtosByFromUserIdAndPending(fromUserId);
    }

    //userId가 받은 모든 친구요청 조회
    @Transactional(readOnly = true)
    public List<FriendInfoDto> getFriendRequestsReceivedByUser(Long toUserId) {
        return friendshipRepository.getFriendInfoDtosByToUserIdAndPending(toUserId);
    }

    //userId의 친구관계 삭제
    @Transactional
    public void deleteFriend(Long friendshipId, Long userId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new ErrorException(Error.FRIENDSHIP_NOT_FOUND));
        if (friendship.getStatus() != FriendshipStatus.ACCEPTED
                && !friendship.getToUserId().equals(userId)
                && !friendship.getFromUserId().equals(userId)) {
            throw new ErrorException(Error.NOT_FRIEND);
        }
        friendshipRepository.delete(friendship);
    }
}
