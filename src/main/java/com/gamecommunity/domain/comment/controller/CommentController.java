package com.gamecommunity.domain.comment.controller;


import com.gamecommunity.domain.comment.dto.CommentRequestDto;
import com.gamecommunity.domain.comment.dto.CommentResponseDto;
import com.gamecommunity.domain.comment.service.CommentService;
import com.gamecommunity.domain.post.service.PostService;
import com.gamecommunity.domain.user.entity.User;
import com.gamecommunity.global.response.ApiResponse;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

  private final CommentService commentService;

  private final PostService postService;

  @PostMapping
  public ResponseEntity<ApiResponse<CommentResponseDto>> createComment(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long postId,
      @RequestBody CommentRequestDto commentRequestDto
  ) {
    User user = userDetails.getUser();
    System.out.println(commentRequestDto);
    commentService.createComment(user, postId,
        commentRequestDto);
    return ResponseEntity.ok(ApiResponse.ok("댓글 생성 성공", null));
  }

  @PatchMapping("/{commentId}")
  public ResponseEntity<ApiResponse<CommentResponseDto>> updateComment(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long commentId,
      @RequestBody CommentRequestDto commentRequestDto
  ) {
    User user = userDetails.getUser();
    commentService.updateComment(user, commentId,
        commentRequestDto);

    return ResponseEntity.ok(ApiResponse.ok("댓글 수정 성공", null));
  }

  @DeleteMapping("/{commentId}")
  public ResponseEntity<ApiResponse<Void>> deleteComment(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long commentId
  ) {
    User user = userDetails.getUser();
    commentService.deleteComment(user, commentId);

    return ResponseEntity.ok(ApiResponse.ok("댓글 삭제 성공", null));
  }

  @GetMapping
  public ResponseEntity<ApiResponse<Page<CommentResponseDto>>> getComments(
      @RequestParam("page") int page,
      @RequestParam("size") int size,
      @PathVariable Long postId) {
    Page<CommentResponseDto> commentResponseDto = commentService.getComments(page - 1, size,
        postId);
    return ResponseEntity.ok(ApiResponse.ok("댓글 조회 성공", commentResponseDto));
  }

  // 댓글 채택 API
  @PutMapping("/{commentId}/accept")
  public ResponseEntity<?> acceptComment(
      @PathVariable Long postId,
      @PathVariable Long commentId,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    postService.acceptComment(postId, commentId, userDetails);
    return ResponseEntity.ok(ApiResponse.ok("댓글 채택 성공", null));
  }

}
