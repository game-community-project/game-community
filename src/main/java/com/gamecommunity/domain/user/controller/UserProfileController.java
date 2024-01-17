package com.gamecommunity.domain.user.controller;

import com.gamecommunity.domain.user.dto.ModifyProfileDto;
import com.gamecommunity.domain.user.service.UserProfileService;
import com.gamecommunity.global.response.ApiResponse;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

  @PatchMapping("/{userId}")
  public ResponseEntity<ApiResponse> modifyProfile(
          @PathVariable Long userId,
          @RequestBody @Valid ModifyProfileDto modifyProfileDto,
          @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    userProfileService.modifyProfile(userId, modifyProfileDto.introduction(),
            modifyProfileDto.profile_url(), userDetails);

    return ResponseEntity.ok(ApiResponse.ok("프로필 수정 성공", null));
  }

}
