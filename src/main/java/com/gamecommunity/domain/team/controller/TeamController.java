package com.gamecommunity.domain.team.controller;

import com.gamecommunity.domain.team.dto.TeamRequestDto;
import com.gamecommunity.domain.team.dto.TeamResponseDto;
import com.gamecommunity.domain.team.service.TeamService;
import com.gamecommunity.global.exception.common.BusinessException;
import com.gamecommunity.global.exception.common.ErrorCode;
import com.gamecommunity.global.response.ApiResponse;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
  public ResponseEntity<ApiResponse<TeamResponseDto>> createTeam(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody TeamRequestDto teamRequestDto) {
    TeamResponseDto teamResponseDto = teamService.createTeam(userDetails.getUser(), teamRequestDto);
    return ResponseEntity.ok(ApiResponse.ok("팀 생성 성공", teamResponseDto));
  }


  @GetMapping("/{teamId}")
  public ResponseEntity<ApiResponse<TeamResponseDto>> getTeam(@PathVariable Long teamId) {
    TeamResponseDto teamResponseDto = teamService.getTeam(teamId);
    return ResponseEntity.ok(ApiResponse.ok("팀 정보 조회 성공", teamResponseDto));
  }

  @GetMapping("/{teamId}/users")
  public ResponseEntity<ApiResponse<List<String>>> getUsersFromTeam(@PathVariable Long teamId) {
    List<String> nicknameList = teamService.getUsersFromTeam(teamId);
    return ResponseEntity.ok(ApiResponse.ok("팀에 속해 있는 유저 이름 리스트 조회 성공", nicknameList));
  }

  @GetMapping("/users")
  public ResponseEntity<ApiResponse<Page<TeamResponseDto>>> getTeamsByUser(
      @RequestParam("page") int page,
      @RequestParam("size") int size,
      @RequestParam("sortBy") String sortBy,
      @RequestParam("isAsc") boolean isAsc,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    Page<TeamResponseDto> teamResponseDtoList = teamService.getTeamsByUser(page - 1, size, sortBy,
        isAsc, userDetails.getUser());
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

  @GetMapping("/{teamId}/admin")
  public ResponseEntity<ApiResponse<Boolean>> isTeamAdmin(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long teamId) {
    if (userDetails == null) {
      throw new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.AUTHENTICATION_EXCEPTION);
    }
    Boolean isTeamAdmin = teamService.isTeamAdmin(userDetails.getUser(), teamId);
    return ResponseEntity.ok(ApiResponse.ok("팀의 관리자 여부 확인 성공", isTeamAdmin));
  }
}


