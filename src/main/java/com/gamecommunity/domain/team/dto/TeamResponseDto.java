package com.gamecommunity.domain.team.dto;

import com.gamecommunity.domain.team.entity.Team;
import com.gamecommunity.domain.teamuser.entity.TeamUser;
import com.gamecommunity.global.enums.game.name.GameName;

public record TeamResponseDto(
    String teamName,
    String teamIntroduction,
    GameName gameName
) {

  public TeamResponseDto(Team team) {
    this(team.getTeamName(),
        team.getTeamIntroduction(),
        team.getGameName());
  }

  public TeamResponseDto(TeamUser teamUser) {
    this(teamUser.getTeam().getTeamName(),
        teamUser.getTeam().getTeamIntroduction(),
        teamUser.getTeam().getGameName());
  }
}
