package com.gamecommunity.domain.admin.controller;

import com.gamecommunity.domain.admin.dto.UserBlockRequestDto;
import com.gamecommunity.domain.admin.service.AdminService;
import com.gamecommunity.global.enums.game.name.GameName;
import com.gamecommunity.global.enums.game.type.GameType;
import com.gamecommunity.domain.post.dto.PostRequestDto;
import com.gamecommunity.global.response.ApiResponse;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

  private final AdminService adminService;

  @GetMapping("/users")
  public ResponseEntity<ApiResponse> getUsers(
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    var usersDto = adminService.getUsers(userDetails);
    return ResponseEntity.ok(ApiResponse.ok("유저 정보 목록 조회 성공", usersDto));
  }

  @GetMapping("/users/{nickname}")
  public ResponseEntity<ApiResponse> getUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable String nickname) {
    var userDto = adminService.getUser(userDetails, nickname);
    return ResponseEntity.ok(ApiResponse.ok("유저 정보 조회 성공", userDto));
  }

  @DeleteMapping("/users/{userId}")
  public ResponseEntity<ApiResponse> deleteUser(
      @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable long userId) {
    adminService.deleteUser(userDetails, userId);
    return ResponseEntity.ok(ApiResponse.ok("유저 삭제 성공", null));
  }

  @PatchMapping("/users/block")
  public ResponseEntity<ApiResponse> setBlock(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody UserBlockRequestDto userBlockRequestDto) {
    adminService.setBlock(userDetails, userBlockRequestDto);
    return ResponseEntity.ok(ApiResponse.ok("유저 차단 성공", null));
  }

  @GetMapping("/posts/report")
  public ResponseEntity<ApiResponse> getReportedPosts(
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    var postsDto = adminService.getReportedPosts(userDetails);
    return ResponseEntity.ok(ApiResponse.ok("신고 게시물 목록 조회 성공", postsDto));
  }

  @GetMapping("/posts/report/{postId}")
  public ResponseEntity<ApiResponse> getReportedPost(
      @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable long postId) {
    var postDto = adminService.getReportedPost(userDetails, postId);
    return ResponseEntity.ok(ApiResponse.ok("신고 게시물 조회 성공", postDto));
  }

  @PostMapping("/notices")
  public ResponseEntity<ApiResponse> writeNotice(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody PostRequestDto requestDto,
      @RequestParam(required = false) GameType gameType,
      @RequestParam(required = false) GameName gameName
  ) {
    adminService.writeNotice(userDetails, requestDto, gameType, gameName);
    return ResponseEntity.ok(ApiResponse.ok("공지사항 작성 성공", null));
  }
}
