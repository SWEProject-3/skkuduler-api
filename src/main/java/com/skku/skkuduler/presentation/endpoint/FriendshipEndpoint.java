package com.skku.skkuduler.presentation.endpoint;

import com.skku.skkuduler.application.FriendshipService;
import com.skku.skkuduler.common.security.JwtUtil;
import com.skku.skkuduler.dto.request.FriendshipRequestDto;
import com.skku.skkuduler.dto.response.FriendInfoDto;
import com.skku.skkuduler.presentation.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendshipEndpoint {

    private final FriendshipService friendshipService;
    private final JwtUtil jwtUtil;

    //1. 친구 조회
    @GetMapping
    public ApiResponse<List<FriendInfoDto>> getAcceptedFriendRequests(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtUtil.extractUserId(token);
        List<FriendInfoDto> acceptedRequests = friendshipService.getFriends(userId);
        return new ApiResponse<>(acceptedRequests);
    }

    // 친구 삭제
    @DeleteMapping("/{friendshipId}")
    public ApiResponse<Void> removeFriendship(@PathVariable Long friendshipId,
                                              @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtUtil.extractUserId(token);
        friendshipService.deleteFriend(friendshipId,userId);
        return new ApiResponse<>("친구가 성공적으로 삭제되었습니다.");
    }

    // 2. 친구 요청 보내기
    @PostMapping("/requests")
    public ApiResponse<Void> sendFriendRequest(@RequestBody FriendshipRequestDto request,
                                               @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        Long fromUserId = jwtUtil.extractUserId(token);
        String email = request.getEmail();
        friendshipService.sendFriendRequest(fromUserId, email);
        return new ApiResponse<>("친구 요청이 성공적으로 보내졌습니다.");
    }

    //2.1 내가 보낸 친구 요청 조회
    @GetMapping("/requests/sent")
    public ApiResponse<List<FriendInfoDto>> getSentFriendRequest(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        Long fromUserId = jwtUtil.extractUserId(token);
        List<FriendInfoDto> sentRequests = friendshipService.getFriendRequestsSentByUser(fromUserId);
        return new ApiResponse<>(sentRequests);
    }

    //2.2 내가 받은 친구 요청 조회
    @GetMapping("/requests/received")
    public ApiResponse<List<FriendInfoDto>> getReceivedFriendRequest(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        Long toUserId = jwtUtil.extractUserId(token);
        List<FriendInfoDto> receivedRequests = friendshipService.getFriendRequestsReceivedByUser(toUserId);
        return new ApiResponse<>(receivedRequests);
    }

    // 3. 친구 요청 철회
    @DeleteMapping("/requests/sent/{friendshipId}")
    public ApiResponse<Void> cancelPendingFriendRequest(@PathVariable Long friendshipId,
                                                        @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        Long fromUserId = jwtUtil.extractUserId(token);
        friendshipService.cancelPendingFriendRequest(friendshipId, fromUserId);
        return new ApiResponse<>("친구 요청이 성공적으로 철회되었습니다.");
    }

    // 4. 친구 요청 수락 / 거절
    @PutMapping("/requests/received/{friendshipId}")
    public ApiResponse<Void> acceptFriendRequest(@PathVariable Long friendshipId,
                                                 @RequestParam(name = "accept") boolean accept,
                                                 @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        Long toUserId = jwtUtil.extractUserId(token);
        if (accept) {
            friendshipService.acceptFriendRequest(friendshipId, toUserId);
            return new ApiResponse<>("친구 요청이 성공적으로 수락되었습니다.");
        } else {
            friendshipService.rejectFriendRequest(friendshipId, toUserId);
            return new ApiResponse<>("친구 요청이 성공적으로 거절되었습니다");
        }
    }

}
