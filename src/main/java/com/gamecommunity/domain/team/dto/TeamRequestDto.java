package com.gamecommunity.domain.team.dto;

import com.gamecommunity.domain.team.entity.Team;

public record TeamRequestDto(
    String teamName,
    String teamIntroduction
) {

  public TeamRequestDto(Team team) {
    this(team.getTeamName(),
        team.getTeamIntroduction()
    );
  }

}
