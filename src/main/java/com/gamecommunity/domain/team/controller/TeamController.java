package com.gamecommunity.domain.team.controller;

import com.gamecommunity.domain.team.dto.TeamRequestDto;
import com.gamecommunity.domain.team.service.TeamService;
import com.gamecommunity.global.response.ApiResponse;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
}