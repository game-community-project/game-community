package com.gamecommunity.domain.test;


import com.gamecommunity.domain.team.entity.Team;
import com.gamecommunity.domain.user.entity.User;
import com.gamecommunity.global.enums.game.name.GameName;

public interface TeamTest extends UserTest{
  String ANOTHER_PREFIX = "another-";
  Long TEST_TEAM_ID = 1L;
  Long TEST_ANOTHER_TEAM_ID = 2L;
  String TEST_TEAM_NAME = "name1";
  String TEST_TEAM_INTRODUCTION = "intro1";
  String TEST_ANOTHER_TEAM_NAME = "name1";
  String TEST_ANOTHER_TEAM_INTRODUCTION = "intro1";
  Long TEST_TEAM_ADMIN_ID =1L;
  GameName TEST_ANOTHER_GAME_NAME = GameName.VALORANT;
  GameName TEST_GAME_NAME = GameName.LEAGUE_OF_LEGEND;
  User TEST_USER = UserTest.TEST_USER;

  Team TEST_TEAM = Team.builder()
      .teamId(TEST_TEAM_ID)
      .teamName(TEST_TEAM_NAME)
      .teamIntroduction(TEST_TEAM_INTRODUCTION)
      .teamAdminId(TEST_TEAM_ADMIN_ID)
      .build();

  Team ANOTHER_TEST_TEAM = Team.builder()
      .teamId(TEST_ANOTHER_TEAM_ID)
      .teamName(TEST_ANOTHER_TEAM_NAME)
      .teamIntroduction(TEST_ANOTHER_TEAM_INTRODUCTION)
      .teamAdminId(TEST_TEAM_ADMIN_ID)
      .build();



}
