package com.gamecommunity.domain.post.controller;

import com.gamecommunity.domain.post.dto.PostRequestDto;
import com.gamecommunity.domain.post.dto.PostResponseDto;
import com.gamecommunity.domain.post.service.PostService;
import com.gamecommunity.global.enums.board.BoardName;
import com.gamecommunity.global.enums.game.name.GameName;
import com.gamecommunity.global.enums.game.type.GameType;
import com.gamecommunity.global.response.ApiResponse;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
  @PostMapping
  public ResponseEntity<?> createPost(
      @RequestPart(value = "requestDto") PostRequestDto requestDto,
      @RequestPart(value = "file", required = false) MultipartFile file,
      @RequestParam(required = false) GameType gameType,
      @RequestParam(required = false) GameName gameName,
      @RequestParam BoardName boardName,
      @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

    postService.createPost(requestDto, gameType, gameName, boardName, file, userDetails);
    return ResponseEntity.ok(ApiResponse.ok("게시글 작성 성공", null));
  }

  // 게시글 단건 조회
  @GetMapping("/{postId}")
  public ResponseEntity<?> getPost(@PathVariable Long postId) {

    PostResponseDto responseDto = postService.getPost(postId);
    return ResponseEntity.ok(ApiResponse.ok(postId + "번 게시글 조회 성공", responseDto));
  }

}
