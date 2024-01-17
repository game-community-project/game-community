package com.gamecommunity.domain.post.controller;

import com.gamecommunity.domain.post.service.PostLikeService;
import com.gamecommunity.domain.user.entity.User;
import com.gamecommunity.global.config.SecurityConfig.AuthenticationHelper;
import com.gamecommunity.global.response.ApiResponse;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts/{postId}/like")
@RequiredArgsConstructor
public class PostLikeController {

  private final PostLikeService postLikeService;

  // 좋아요 싫어요 하기
  @PostMapping
  public ResponseEntity<?> addLike(
      @PathVariable Long postId,
      @RequestParam Boolean isLike,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {

    postLikeService.addLike(postId, isLike, userDetails);
    String message;

    if (isLike) {
      message = "좋아요";
    } else {
      message = "싫어요";
    }

    return ResponseEntity.ok(ApiResponse.ok(message + " 성공", null));
  }


}
