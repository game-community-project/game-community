package com.gamecommunity.domain.test;

import com.gamecommunity.domain.team.entity.Team;
import com.gamecommunity.domain.teamuser.entity.TeamUser;
import com.gamecommunity.domain.user.entity.User;
import com.gamecommunity.global.enums.game.name.GameName;
import java.util.ArrayList;

public class TeamTestHelper implements UserTest {

  public static Team createFakeTeam(Long teamId, Long adminId, String teamName,
      String teamIntroduction, GameName gameName) {
    Team team = Team.builder()
        .teamId(teamId)
        .teamAdminId(adminId)
        .teamName(teamName)
        .teamIntroduction(teamIntroduction)
        .gameName(gameName)
        .teamUsers(new ArrayList<>())
        .build();

    TeamUser teamUser1 = createFakeTeamUser(team, TEST_USER);
    TeamUser teamUser2 = createFakeTeamUser(team, TEST_ANOTHER_USER);

    team.getTeamUsers().add(teamUser1);
    team.getTeamUsers().add(teamUser2);

    return team;
  }

  public static TeamUser createFakeTeamUser(Team team, User user) {
    TeamUser teamUser = TeamUser.builder()
        .team(team)
        .user(user)
        .build();
    return teamUser;
  }
}
