package com.skku.skkuduler.presentation.endpoint;

import com.skku.skkuduler.application.FriendshipService;
import com.skku.skkuduler.common.security.JwtUtil;
import com.skku.skkuduler.domain.friendship.FriendshipStatus;
import com.skku.skkuduler.presentation.ApiResponse;
import com.skku.skkuduler.domain.user.User;
import com.skku.skkuduler.dto.request.FriendshipRequestDto;
import com.skku.skkuduler.infrastructure.UserRepository;
import com.skku.skkuduler.domain.friendship.Friendship;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<List<Friendship>> getAcceptedFriendRequests(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {

        Long userId = extractUserIdFromToken(token);
        List<Friendship> acceptedRequests = friendshipService.getFriendRequestsByStatus(userId, FriendshipStatus.ACCEPTED);
        return ResponseEntity.ok(acceptedRequests);
    }

    // 2. 친구 요청 보내기
    @PostMapping("/request")
    public ResponseEntity<String> sendFriendRequest(@RequestBody FriendshipRequestDto request,
                                                    @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " 접두사를 제거
        }

        Long userIdFromToken = jwtUtil.extractUserId(token);
        if (!userIdFromToken.equals(request.getFromUserId())) {
            return ResponseEntity.status(403).body("You are not authorized to send this friend request.");
        }


        // fromUserId와 toUserId로 User 객체 조회
        User fromUser = userRepository.findById(request.getFromUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + request.getFromUserId()));
        User toUser = userRepository.findById(request.getToUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + request.getToUserId()));

        try {
            // 친구 요청 보내기
            Friendship friendship = friendshipService.sendFriendRequest(fromUser, toUser);
            return ResponseEntity.ok("Friend request sent successfully with status: " + friendship.getStatus());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    // 3. PENDING중인 친구요청 철회
    @DeleteMapping("/{friendshipId}/cancel")
    public ResponseEntity<String> cancelPendingFriendRequest(
            @PathVariable Long friendshipId,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {

        Long userId = extractUserIdFromToken(token);

        try {
            friendshipService.cancelPendingFriendRequest(friendshipId, userId);
            return ResponseEntity.ok("Friend request canceled successfully.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 4-1 내가 보낸 친구요청 조회하기
    @GetMapping("/sent_requests")
    public ResponseEntity<List<Friendship>> getFriendRequestsSentByUser(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {

        Long userId = extractUserIdFromToken(token);
        List<Friendship> sentRequests = friendshipService.getFriendRequestsSentByUser(userId);
        return ResponseEntity.ok(sentRequests);
    }
    // 4-2 내가 받은 친구요청 조회하기
    @GetMapping("/received_requests")
    public ResponseEntity<List<Friendship>> getFriendRequestsReceivedByUser(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {

        Long userId = extractUserIdFromToken(token);
        List<Friendship> receivedRequests = friendshipService.getFriendRequestsReceivedByUser(userId);
        return ResponseEntity.ok(receivedRequests);
    }

    // 5. 친구요청 수락하기
    @PostMapping("/{friendshipId}/accept")
    public ResponseEntity<String> acceptFriendRequest(
            @PathVariable Long friendshipId,
            @RequestHeader(name=HttpHeaders.AUTHORIZATION) String token) {
        Long userIdFromToken = extractUserIdFromToken(token);

        Friendship friendship = friendshipService.findFriendshipById(friendshipId);

        if (!friendship.getToUserId().equals(userIdFromToken)) {
            return ResponseEntity.status(403).body("You are not authorized to accept this friend request.");
        }

        friendship = friendshipService.acceptFriendRequest(friendshipId);
        return ResponseEntity.ok("Friend request accepted with status: " + friendship.getStatus());
    }

    // 6. 친구요청 거절하기
    @PostMapping("/{friendshipId}/reject")
    public ResponseEntity<String> rejectFriendRequest(@PathVariable Long friendshipId) {
        Friendship friendship = friendshipService.rejectFriendRequest(friendshipId);
        return ResponseEntity.ok("Friend request rejected with status: " + friendship.getStatus());
    }

    // JWT 토큰에서 userId를 추출하는 헬퍼 메서드
    private Long extractUserIdFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " 접두사를 제거
        }
        return jwtUtil.extractUserId(token);
    }

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
