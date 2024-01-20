package com.gamecommunity.domain.team.dto;

import com.gamecommunity.domain.team.entity.Team;
import com.gamecommunity.domain.teamuser.entity.TeamUser;
import com.gamecommunity.domain.user.dto.UserProfileDto;
import com.gamecommunity.global.enums.game.name.GameName;
import com.nimbusds.openid.connect.sdk.UserInfoResponse;
import java.util.List;

public record TeamResponseDto(
    Long teamId,
    String teamName,
    String teamIntroduction,
    Long teamAdminId,
    GameName gameName
) {

  public TeamResponseDto(Team team) {
    this(team.getTeamId(),
        team.getTeamName(),
        team.getTeamIntroduction(),
        team.getTeamAdminId(),
        team.getGameName()
    );
  }

  public TeamResponseDto(TeamUser teamUser) {
    this(teamUser.getTeam().getTeamId(),
        teamUser.getTeam().getTeamName(),
        teamUser.getTeam().getTeamIntroduction(),
        teamUser.getTeam().getTeamAdminId(),
        teamUser.getTeam().getGameName());
  }
}
