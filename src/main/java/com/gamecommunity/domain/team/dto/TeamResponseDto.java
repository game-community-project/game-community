package com.gamecommunity.domain.team.dto;

import com.gamecommunity.domain.team.entity.Team;
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
}
