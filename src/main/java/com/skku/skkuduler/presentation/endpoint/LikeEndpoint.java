package com.skku.skkuduler.presentation.endpoint;

import com.skku.skkuduler.application.LikeService;
import com.skku.skkuduler.common.security.JwtUtil;
import com.skku.skkuduler.presentation.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikeEndpoint {

    private final JwtUtil jwtUtil;
    private final LikeService likeService;
    @PostMapping("/users/events/{eventId}/likes")
    public ApiResponse<Void> likeEvent(@PathVariable("eventId") Long eventId,
                                       @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token){
        Long userId = jwtUtil.extractUserId(token);
        likeService.likeEvent(eventId, userId);
        return new ApiResponse<>("좋아요가 성공적으로 등록 되었습니다.");
    }

    @DeleteMapping("/users/events/{eventId}/likes")
    public ApiResponse<Void> unlikeEvent(@PathVariable("eventId") Long eventId,
                                       @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token){
        Long userId = jwtUtil.extractUserId(token);
        likeService.unlikeEvent(eventId, userId);
        return new ApiResponse<>("좋아요가 성공적으로 취소 되었습니다.");
    }
}
