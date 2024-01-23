package com.gamecommunity.domain.team.dto;

import com.gamecommunity.domain.team.entity.Team;
import com.gamecommunity.domain.teamuser.entity.TeamUser;

public record TeamResponseDto(
    Long teamId,
    String teamName,
    String teamIntroduction,
    Long teamAdminId
) {

  public TeamResponseDto(Team team) {
    this(team.getTeamId(),
        team.getTeamName(),
        team.getTeamIntroduction(),
        team.getTeamAdminId()
    );
  }

  public TeamResponseDto(TeamUser teamUser) {
    this(teamUser.getTeam().getTeamId(),
        teamUser.getTeam().getTeamName(),
        teamUser.getTeam().getTeamIntroduction(),
        teamUser.getTeam().getTeamAdminId());
  }
}
