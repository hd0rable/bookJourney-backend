package com.example.bookjourneybackend.domain.comment.controller;

import com.example.bookjourneybackend.domain.comment.domain.dto.request.PostCommentRequest;
import com.example.bookjourneybackend.domain.comment.domain.dto.response.GetCommentListResponse;
import com.example.bookjourneybackend.domain.comment.domain.dto.response.PostCommentResponse;
import com.example.bookjourneybackend.domain.comment.service.CommentService;
import com.example.bookjourneybackend.global.annotation.LoginUserId;
import com.example.bookjourneybackend.global.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{recordId}")
    public BaseResponse<GetCommentListResponse> getComments(
            @PathVariable final Long recordId,
            @LoginUserId final Long userId
    ) {
        return BaseResponse.ok(commentService.showComments(recordId, userId));
    }

    @PostMapping("/{recordId}")
    public BaseResponse<PostCommentResponse> postComment(
            @PathVariable final Long recordId,
            @LoginUserId final Long userId,
            @RequestBody @Valid final PostCommentRequest postCommentRequest
    ) {
        return BaseResponse.ok(commentService.createComment(recordId, userId, postCommentRequest));
    }
}
