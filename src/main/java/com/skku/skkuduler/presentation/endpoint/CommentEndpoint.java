package com.skku.skkuduler.presentation.endpoint;

import com.skku.skkuduler.application.CommentService;
import com.skku.skkuduler.common.security.JwtUtil;
import com.skku.skkuduler.dto.request.CommentCreateDto;
import com.skku.skkuduler.dto.request.CommentUpdateDto;
import com.skku.skkuduler.dto.response.CommentInfo;
import com.skku.skkuduler.presentation.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentEndpoint {


    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    @GetMapping("/users/events/{eventId}/comments")
    public ApiResponse<Page<CommentInfo>> getComments(@PathVariable("eventId") Long eventId,
                                                      @RequestParam(value = "page", required = false) Integer page,
                                                      @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        Long viewerId = jwtUtil.extractUserId(token);
        Pageable p = page == null ? Pageable.unpaged() : PageRequest.of(page, 20);
        return new ApiResponse<>(commentService.getComments(eventId, viewerId, p));
    }

    @PostMapping("/users/events/{eventId}/comments")
    public ApiResponse<Void> createComment(@PathVariable("eventId") Long eventId,
                                           @RequestBody CommentCreateDto commentCreateDto,
                                           @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtUtil.extractUserId(token);
        commentService.createComment(eventId, userId, commentCreateDto.getContent());
        return new ApiResponse<>("댓글이 성공적으로 등록되었습니다.");
    }

    @PutMapping("/users/events/comments/{commentId}")
    public ApiResponse<Void> updateComments(@PathVariable("commentId") Long commentId,
                                            @RequestBody CommentUpdateDto commentUpdateDto,
                                            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {

        Long userId = jwtUtil.extractUserId(token);
        commentService.updateComment(commentId, userId, commentUpdateDto.getContent());
        return new ApiResponse<>("댓글이 성공적으로 업데이트 되었습니다.");
    }

    @DeleteMapping("/users/events/comments/{commentId}")
    public ApiResponse<Void> deleteComments(@PathVariable("commentId") Long commentId,
                                            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token) {

        Long userId = jwtUtil.extractUserId(token);
        commentService.deleteComment(commentId, userId);
        return new ApiResponse<>("댓글이 성공적으로 삭제 되었습니다.");
    }
}
