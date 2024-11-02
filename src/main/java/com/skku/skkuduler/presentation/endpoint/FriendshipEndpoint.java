package com.skku.skkuduler.presentation.endpoint;

import com.skku.skkuduler.application.FriendshipService;
import com.skku.skkuduler.common.exception.Error;
import com.skku.skkuduler.common.exception.ErrorException;
import com.skku.skkuduler.common.security.JwtUtil;
import com.skku.skkuduler.domain.friendship.Friendship.FriendshipStatus;
import com.skku.skkuduler.dto.response.FriendshipResponseDto;
import com.skku.skkuduler.presentation.ApiResponse;
import com.skku.skkuduler.domain.user.User;
import com.skku.skkuduler.dto.request.FriendshipRequestDto;
import com.skku.skkuduler.infrastructure.UserRepository;
import com.skku.skkuduler.domain.friendship.Friendship;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import java.util.List;

@RestController
@RequestMapping("/api/friendship")
@RequiredArgsConstructor
public class FriendshipEndpoint {

    private final FriendshipService friendshipService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    //1. 친구 조회
    @GetMapping("/friends")
    public ApiResponse<List<FriendshipResponseDto>> getAcceptedFriendRequests(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {

        Long userId = jwtUtil.extractUserId(token);
        List<FriendshipResponseDto> acceptedRequests = friendshipService.getFriendRequestsByStatus(userId, FriendshipStatus.ACCEPTED);

        return new ApiResponse<>(acceptedRequests);
    }

    // 2. 친구 요청 보내기
    @PostMapping("/request")
    public ApiResponse<String> sendFriendRequest(@RequestBody FriendshipRequestDto request,
                                                    @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {

        Long userIdFromToken = jwtUtil.extractUserId(token);

        if (!userIdFromToken.equals(request.getFromUserId())) {
            return new ApiResponse<>(400, "You are not authorized to send this friend request.");
        }

        // fromUserId와 toUserId로 User 객체 조회
        User fromUser = userRepository.findById(request.getFromUserId())
                .orElseThrow(() -> new ErrorException(Error.USER_NOT_FOUND));
        User toUser = userRepository.findById(request.getToUserId())
                .orElseThrow(() -> new ErrorException(Error.USER_NOT_FOUND));

        // 친구 요청 보내기
        FriendshipResponseDto friendship = friendshipService.sendFriendRequest(fromUser, toUser);
        return new ApiResponse<>("Friend request sent successfully");

    }



    // 3. PENDING중인 친구요청 철회
    @PostMapping("/{friendshipId}/cancellation")
    public ApiResponse<String> cancelPendingFriendRequest(
            @PathVariable Long friendshipId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {

        Long userIdFromToken = jwtUtil.extractUserId(token);

        try {
            friendshipService.cancelPendingFriendRequest(friendshipId, userIdFromToken);
            return new ApiResponse<>("Friend request canceled successfully.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            return new ApiResponse<>(400, "Friend request cancellation failed.");
        }
    }

    // 4-1 내가 보낸 친구요청 조회하기
    @GetMapping("/sent_requests")
    public ApiResponse<List<FriendshipResponseDto>> getFriendRequestsSentByUser(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {

        Long userId = jwtUtil.extractUserId(token);
        List<FriendshipResponseDto> sentRequests = friendshipService.getFriendRequestsSentByUser(userId);
        return new ApiResponse<>(sentRequests);
    }
    // 4-2 내가 받은 친구요청 조회하기
    @GetMapping("/received_requests")
    public ApiResponse<List<FriendshipResponseDto>> getFriendRequestsReceivedByUser(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {

        Long userId = jwtUtil.extractUserId(token);
        List<FriendshipResponseDto> receivedRequests = friendshipService.getFriendRequestsReceivedByUser(userId);
        return new ApiResponse<>(receivedRequests);
    }

    // 5. 친구요청 수락하기
    @PostMapping("/{friendshipId}/acceptance")
    public ApiResponse<String> acceptFriendRequest(
            @PathVariable Long friendshipId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {

        Long userIdFromToken = jwtUtil.extractUserId(token);
        FriendshipResponseDto friendship = friendshipService.findFriendshipById(friendshipId);

        if (!friendship.getToUserId().equals(userIdFromToken)) {
            return new ApiResponse<>("You are not authorized to accept this friend request.");
        }

        FriendshipResponseDto acceptedFriendship = friendshipService.acceptFriendRequest(friendshipId);
        String message = "Friend request accepted with status: " + acceptedFriendship.getStatus();
        return new ApiResponse<>(HttpStatus.OK.value(), message);
    }

    // 6. 친구요청 거절하기
    @PostMapping("/{friendshipId}/rejection")
    public ApiResponse<String> rejectFriendRequest(@PathVariable Long friendshipId) {
        FriendshipResponseDto rejectedFriendship = friendshipService.rejectFriendRequest(friendshipId);
        String message = "Friend request rejected Successfully" + rejectedFriendship.getStatus();
        return new ApiResponse<>(HttpStatus.OK.value(), message);
    }

    // JWT 토큰에서 userId를 추출하는 헬퍼 메서드


//    @GetMapping("/pending")
//    public ResponseEntity<List<Friendship>> getPendingFriendRequests(
//            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
//
//        // JWT 토큰에서 userId 추출
//        Long userId = extractUserIdFromToken(token);
//        List<Friendship> pendingRequests = friendshipService.getFriendRequestsByStatus(userId, FriendshipStatus.PENDING);
//        return ResponseEntity.ok(pendingRequests);
//    }
//
//    @GetMapping("/rejected")
//    public ResponseEntity<List<Friendship>> getRejectedFriendRequests(
//            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
//
//        Long userId = extractUserIdFromToken(token);
//        List<Friendship> rejectedRequests = friendshipService.getFriendRequestsByStatus(userId, FriendshipStatus.REJECTED);
//        return ResponseEntity.ok(rejectedRequests);
//    }
//
//    @GetMapping("/all")
//    public ResponseEntity<List<Friendship>> getAllFriendRequests(
//            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
//
//        Long userId = extractUserIdFromToken(token);
//        List<Friendship> allRequests = friendshipService.getAllFriendRequests(userId);
//        return ResponseEntity.ok(allRequests);
//    }
}
