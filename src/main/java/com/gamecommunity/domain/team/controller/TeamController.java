package com.gamecommunity.domain.team.controller;

import com.gamecommunity.domain.team.dto.TeamRequestDto;
import com.gamecommunity.domain.team.dto.TeamResponseDto;
import com.gamecommunity.domain.team.service.TeamService;
import com.gamecommunity.global.enums.game.name.GameName;
import com.gamecommunity.global.response.ApiResponse;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    return ResponseEntity.ok(ApiResponse.ok("그룹 생성 성공", null));
  }

  @GetMapping
  public ResponseEntity<ApiResponse<Map<GameName, List<TeamResponseDto>>>> getTeam() {
    Map<GameName, List<TeamResponseDto>> teamMap = teamService.getTeamsByGame();
    return ResponseEntity.ok(ApiResponse.ok("게임 별로 속해 있는 그룹 목록 조회 성공", teamMap));
  }

  @GetMapping("/users")
  public ResponseEntity<ApiResponse<List<TeamResponseDto>>> getTeam(
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    List<TeamResponseDto> teamResponseDtoList = teamService.getTeamsByUser(userDetails.getUser());
    return ResponseEntity.ok(ApiResponse.ok("유저가 속한 그룹 목록 조회 성공", teamResponseDtoList));
  }
}