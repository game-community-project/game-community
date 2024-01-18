package com.gamecommunity.domain.team.dto;

import com.gamecommunity.domain.team.entity.Team;
import com.gamecommunity.global.enums.game.name.GameName;

public record TeamRequestDto(
    String name,
    String introduction,
    GameName gameName
) {
  public TeamRequestDto(Team team) {
    this(team.getTeamName(),
        team.getTeamIntroduction(),
        team.getGameName());
  }

}
