package com.gamecommunity.domain.teamuser.controller;

import com.gamecommunity.domain.teamuser.service.TeamUserService;
import com.gamecommunity.global.response.ApiResponse;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
@AllArgsConstructor
public class TeamUserController {

  private final TeamUserService teamUserService;

  @PostMapping("/teams/{teamId}/users/{userId}")
  public ResponseEntity<ApiResponse<Void>> addUserToTeam(
      @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long teamId,
      @PathVariable Long userId) {
    teamUserService.addUserToTeam(userDetails.getUser(), teamId, userId);
    return ResponseEntity.ok(ApiResponse.ok("팀에 유저 추가 성공", null));
  }

  @DeleteMapping("/teams/{teamId}/users/{userId}")
  public ResponseEntity<ApiResponse<Void>> deleteUserFromTeam(
      @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long teamId,
      @PathVariable Long userId) {
    teamUserService.deleteUserFromTeam(userDetails.getUser(), teamId, userId);
    return ResponseEntity.ok(ApiResponse.ok("팀에 유저 삭제 성공", null));
  }

  @DeleteMapping("/api/teams/{teamId}/leave")
  public ResponseEntity<ApiResponse<Void>> leaveFromTeam(
      @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long teamId){

    teamUserService.leaveFromTeam(userDetails.getUser(),teamId);
    return ResponseEntity.ok(ApiResponse.ok("팀 탈퇴 성공", null));
  }
}
