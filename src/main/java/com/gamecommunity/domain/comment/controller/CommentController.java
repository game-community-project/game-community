package com.gamecommunity.domain.comment.controller;


import com.gamecommunity.domain.comment.dto.CommentRequestDto;
import com.gamecommunity.domain.comment.dto.CommentResponseDto;
import com.gamecommunity.domain.comment.service.CommentService;
import com.gamecommunity.domain.user.entity.User;
import com.gamecommunity.global.response.ApiResponse;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

  private final CommentService commentService;

  @PostMapping
  public ResponseEntity<ApiResponse<CommentResponseDto>> createComment(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long postId,
      @RequestBody CommentRequestDto commentRequestDto
  ) {
    User user = userDetails.getUser();
    CommentResponseDto commentResponseDto = commentService.createComment(user, postId,
        commentRequestDto);
    return ResponseEntity.ok(ApiResponse.ok("댓글 생성 성공", null));
  }
}
