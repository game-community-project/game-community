package com.gamecommunity.domain.post.controller;

import com.gamecommunity.domain.post.dto.PostRequestDto;
import com.gamecommunity.domain.post.dto.PostResponseDto;
import com.gamecommunity.domain.post.service.PostService;
import com.gamecommunity.global.aop.Timer;
import com.gamecommunity.global.response.ApiResponse;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  // 게시글 작성
  @Timer
  @PostMapping
  public ResponseEntity<?> createPost(
      @RequestPart(value = "requestDto") PostRequestDto requestDto,
      @RequestPart(value = "file", required = false) MultipartFile file,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) throws IOException {

    PostResponseDto responseDto = postService.createPost(
        requestDto, file, userDetails);
    return ResponseEntity.ok(ApiResponse.ok("게시글 작성 성공", responseDto));
  }

  // 게시글 단건 조회
  @Timer
  @GetMapping("/{postId}")
  public ResponseEntity<?> getPost(@PathVariable Long postId) {

    PostResponseDto responseDto = postService.getPost(postId);
    return ResponseEntity.ok(ApiResponse.ok(postId + "번 게시글 조회 성공", responseDto));
  }

  // 게시글 페이징 조회
  @Timer
  @GetMapping
  public ResponseEntity<?> getPosts(
      @RequestParam("page") int page,
      @RequestParam("size") int size,
      @RequestParam("sortKey") String sortKey,
      @RequestParam("isAsc") boolean isAsc
  ) {

    Page<PostResponseDto> responseDtoPage = postService.getPosts(
        page - 1, size, sortKey, isAsc);

    return ResponseEntity.ok(ApiResponse.ok("게시글 페이징 조회 성공", responseDtoPage));
  }

  // 게시글 수정
  @Timer
  @PatchMapping("/{postId}")
  public ResponseEntity<?> updatePost(
      @PathVariable Long postId,
      @RequestPart(value = "requestDto") PostRequestDto requestDto,
      @RequestPart(value = "file", required = false) MultipartFile file,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) throws IOException {

    PostResponseDto responseDto = postService.updatePost(postId, requestDto, file, userDetails);
    return ResponseEntity.ok(ApiResponse.ok("게시글 수정 성공", responseDto));
  }

  // 게시글 삭제
  @Timer
  @DeleteMapping("/{postId}")
  public ResponseEntity<?> deletePost(
      @PathVariable Long postId,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {

    postService.deletePost(postId, userDetails);
    return ResponseEntity.ok(ApiResponse.ok("게시글 삭제 성공", null));
  }

  // 게시글 마감
  @Timer
  @PutMapping("/{postId}/close")
  public ResponseEntity<?> closePost(
      @PathVariable Long postId,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {

    postService.closePost(postId);
    return ResponseEntity.ok(ApiResponse.ok("게시글 마감 성공", null));
  }

}
