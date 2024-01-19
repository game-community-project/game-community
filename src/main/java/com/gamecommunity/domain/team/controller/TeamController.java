package com.gamecommunity.domain.team.controller;

import com.gamecommunity.domain.team.dto.TeamRequestDto;
import com.gamecommunity.domain.team.dto.TeamResponseDto;
import com.gamecommunity.domain.team.service.TeamService;
import com.gamecommunity.global.enums.game.name.GameName;
import com.gamecommunity.global.response.ApiResponse;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
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
@RequestMapping("/api/teams")
@AllArgsConstructor
public class TeamController {

  private final TeamService teamService;

  @PostMapping
  public ResponseEntity<ApiResponse<Void>> createTeam(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody TeamRequestDto teamRequestDto) {
    teamService.createTeam(userDetails.getUser(), teamRequestDto);
    return ResponseEntity.ok(ApiResponse.ok("팀 생성 성공", null));
  }



  @GetMapping
  public ResponseEntity<ApiResponse<Page<TeamResponseDto>>> getTeams(
      @RequestParam("page") int page,
      @RequestParam("size") int size,
      @RequestParam("sortBy") String sortBy,
      @RequestParam("isAsc") boolean isAsc,
      @RequestParam("gameName") GameName gameName
  ) {
    Page<TeamResponseDto> teamResponseDtos = teamService.getTeamsByGameName(page-1, size,sortBy,isAsc,gameName);
    return ResponseEntity.ok(ApiResponse.ok("게임 별로 속해 있는 팀 목록 조회 성공", teamResponseDtos));
  }

  @GetMapping("/users")
  public ResponseEntity<ApiResponse<Page<TeamResponseDto>>> getTeams(
      @RequestParam("page") int page,
      @RequestParam("size") int size,
      @RequestParam("sortBy") String sortBy,
      @RequestParam("isAsc") boolean isAsc,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    Page<TeamResponseDto> teamResponseDtoList = teamService.getTeamsByUser(page-1, size,sortBy,isAsc,userDetails.getUser());
    return ResponseEntity.ok(ApiResponse.ok("유저가 속한 팀 조회 성공", teamResponseDtoList));
  }

  @DeleteMapping("/{teamId}")
  public ResponseEntity<ApiResponse<Void>> deleteTeam(
      @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long teamId) {
    teamService.deleteTeam(userDetails.getUser(), teamId);
    return ResponseEntity.ok(ApiResponse.ok("팀 삭제 성공", null));
  }

  @PatchMapping("/{teamId}")
  public ResponseEntity<ApiResponse<Void>> updateTeam(
      @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long teamId,
      @RequestBody TeamRequestDto teamRequestDto) {
    teamService.updateTeam(userDetails.getUser(), teamId, teamRequestDto);
    return ResponseEntity.ok(ApiResponse.ok("팀 수정 성공", null));
  }

  @PostMapping("/teams/{teamId}/users/{userId}")
  public ResponseEntity<ApiResponse<Void>> addUserToTeam(
      @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long teamId,
      @PathVariable Long userId) {
    teamService.addUserToTeam(userDetails.getUser(), teamId, userId);
    return ResponseEntity.ok(ApiResponse.ok("팀에 유저 추가 성공", null));
  }

  @DeleteMapping("/teams/{teamId}/users/{userId}")
  public ResponseEntity<ApiResponse<Void>> DeleteUserFromTeam(
      @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long teamId,
      @PathVariable Long userId) {
    teamService.deleteUserFromTeam(userDetails.getUser(), teamId, userId);
    return ResponseEntity.ok(ApiResponse.ok("팀에 유저 삭제 성공", null));
  }
}
