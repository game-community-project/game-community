package com.gamecommunity.domain.test;


import com.gamecommunity.domain.team.entity.Team;
import com.gamecommunity.domain.teamuser.entity.TeamUser;
import com.gamecommunity.domain.teamuser.entity.TeamUserId;
import com.gamecommunity.domain.user.entity.User;

public interface TeamUserTest extends UserTest,TeamTest{
  User TEST_USER = UserTest.TEST_USER;
  Team TEST_TEAM = TeamTest.TEST_TEAM;
  Team TEST_ANOTHER_TEAM = TeamTest.ANOTHER_TEST_TEAM;


  TeamUserId TEST_TEAM_USER_ID = TeamUserId.builder()
      .teamId(TEST_TEAM_ID)
      .userId(TEST_USER_ID)
      .build();

  TeamUserId TEST_ANOTHER_TEAM_USER_ID = TeamUserId.builder()
      .teamId(TEST_ANOTHER_TEAM_ID)
      .userId(TEST_USER_ID)
      .build();

  TeamUser TEST_TEAM_USER = TeamUser.builder()
      .id(TEST_TEAM_USER_ID)
      .team(TeamTest.TEST_TEAM)
      .user(TEST_USER)
      .build();

  TeamUser TEST_ANOTHER_TEAM_USER = TeamUser.builder()
      .id(TEST_ANOTHER_TEAM_USER_ID)
      .team(TeamTest.TEST_TEAM)
      .user(TEST_USER)
      .build();
}
