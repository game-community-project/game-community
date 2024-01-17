package com.gamecommunity.domain.user.controller;

import com.gamecommunity.domain.user.service.UserProfileService;
import com.gamecommunity.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/profile")
public class UserProfileController {

  private final UserProfileService userProfileService;

  @GetMapping("/{userId}")
  public ResponseEntity<ApiResponse> getProfile(
          @PathVariable Long userId
  ) {
    return ResponseEntity.ok(ApiResponse.ok("프로필 조회 성공", userProfileService.getProfile(userId)));
  }


}
