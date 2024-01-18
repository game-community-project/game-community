package com.gamecommunity.domain.team.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.gamecommunity.domain.team.dto.TeamRequestDto;
import com.gamecommunity.domain.team.dto.TeamResponseDto;
import com.gamecommunity.domain.team.entity.Team;
import com.gamecommunity.domain.team.repository.TeamRepository;
import com.gamecommunity.domain.team.service.TeamService;
import com.gamecommunity.domain.teamuser.entity.TeamUser;
import com.gamecommunity.domain.teamuser.repository.TeamUserRepository;
import com.gamecommunity.domain.test.TeamUserTest;
import com.gamecommunity.domain.test.UserTest;
import com.gamecommunity.domain.user.entity.User;
import com.gamecommunity.global.enums.game.name.GameName;
import com.gamecommunity.global.exception.common.BusinessException;
import com.gamecommunity.global.exception.common.ErrorCode;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("팀 서비스 테스트")
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class TeamServiceTest implements TeamUserTest {

  @InjectMocks
  private TeamService teamService;
  @Mock
  private TeamRepository teamRepository;
  @Mock
  private TeamUserRepository teamUserRepository;


  @Test
  @DisplayName("팀 생성 - 성공")
  void createTeamTestSuccess() {

    // given
    TeamRequestDto teamRequestDto = new TeamRequestDto(TEST_TEAM_NAME,
        TEST_TEAM_INTRODUCTION, TEST_GAME_NAME);
    User loginUser = TEST_USER;

    // when
    teamService.createTeam(loginUser, teamRequestDto);

    // Then
    verify(teamRepository, times(1)).save(any(Team.class));
  }

  @Test
  @DisplayName("게임별 게시글 조회 - 성공")
  void getTeamsByGameTestSuccess() {

    // Given
    Long teamId = TEST_TEAM_ID;
    Team team1 = TEST_TEAM;
    Team team2 = TEST_TEAM;
    GameName gameName = GameName.LEAGUE_OF_LEGEND;
    List<Team> teamList = Arrays.asList(team1, team2);

    given(teamRepository.findAllByGameName(gameName)).willReturn(teamList);

    // When
    List<TeamResponseDto> result = teamService.getTeamsByGameName(gameName);

    // Then
    assertNotNull(result);
    assertFalse(result.isEmpty());

    TeamResponseDto firstTeamResponseDto = result.get(0);
    assertEquals(team1.getTeamName(), firstTeamResponseDto.teamName());
    assertEquals(team1.getTeamIntroduction(), firstTeamResponseDto.teamIntroduction());
  }

  @Test
  @DisplayName("유저별 팀 조회 - 성공")
  void getTeamsByUserTestSuccess() {

    // Given
    User user = UserTest.TEST_USER;
    TeamUser teamUser1 = TeamUserTest.TEST_TEAM_USER;
    TeamUser teamUser2 = TeamUserTest.TEST_ANOTHER_TEAM_USER;
    // 가상의 데이터 리스트 생성
    List<TeamUser> teamUserList = Arrays.asList(teamUser1, teamUser2);

// Mock 객체의 findAllByGameName 메서드 호출에 대한 동작 설정
    given(teamUserRepository.findAllByUserId(user.getId())).willReturn(teamUserList);

    // When
    List<TeamResponseDto> result = teamService.getTeamsByUser(user);

    // Then
    assertNotNull(result);
    assertFalse(result.isEmpty());

    assertEquals(teamUser1.getTeam().getTeamName(), result.get(0).teamName());
  }

  @Test
  @DisplayName("팀 정보 수정  - 성공")
  void updateTeamTestSuccess() {

    // given
    TeamRequestDto teamRequestDto = new TeamRequestDto(TEST_ANOTHER_TEAM_NAME,
        TEST_ANOTHER_TEAM_INTRODUCTION, TEST_ANOTHER_GAME_NAME);
    Long teamId = TEST_TEAM_ID;
    User loginUser = TEST_USER;
    Team team = TEST_TEAM;

    given(teamRepository.findByTeamId(teamId)).willReturn(Optional.of(team));

    // when
    teamService.updateTeam(loginUser, teamId, teamRequestDto);

    // Then
    assertEquals(team.getTeamName(), TEST_ANOTHER_TEAM.getTeamName());
  }

  @Test
  @DisplayName("팀 정보 수정(수정 권한이 없음)  - 실패")
  void updateTeamTestFail() {
    // given
    TeamRequestDto teamRequestDto = new TeamRequestDto(TEST_ANOTHER_TEAM_NAME,
        TEST_ANOTHER_TEAM_INTRODUCTION, TEST_ANOTHER_GAME_NAME);
    Long teamId = TEST_TEAM_ID;
    User loginUser = TEST_ANOTHER_USER;
    Team team = TEST_TEAM;

    given(teamRepository.findByTeamId(teamId)).willReturn(Optional.of(team));

    // when
    BusinessException ex = assertThrows(BusinessException.class, () -> {
      teamService.updateTeam(loginUser, teamId, teamRequestDto);
    });

    // Then
    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    assertEquals(ErrorCode.AUTHENTICATION_MISMATCH_EXCEPTION.getMessage(), ex.getMessage());
  }

  @Test
  @DisplayName("팀 삭제 - 성공")
  void deleteTeamTestSuccess() {

    // given
    TeamRequestDto teamRequestDto = new TeamRequestDto(TEST_ANOTHER_TEAM_NAME,
        TEST_ANOTHER_TEAM_INTRODUCTION, TEST_ANOTHER_GAME_NAME);
    Long teamId = TEST_TEAM_ID;
    User loginUser = TEST_USER;
    Team team = TEST_TEAM;

    given(teamRepository.findByTeamId(teamId)).willReturn(Optional.of(team));

    // when
    teamService.deleteTeam(loginUser, teamId);

    // Then
    verify(teamRepository, times(1)).delete(team);
  }

  @Test
  @DisplayName("팀 삭제 - 실패(수정 권한이 없음)")
  void deleteTeamFailSuccess() {

    // given
    Long teamId = TEST_TEAM_ID;
    User loginUser = TEST_ANOTHER_USER;
    Team team = TEST_TEAM;

    given(teamRepository.findByTeamId(teamId)).willReturn(Optional.of(team));

    // when
    BusinessException ex = assertThrows(BusinessException.class, () -> {
      teamService.deleteTeam(loginUser, teamId);
    });

    // Then
    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    assertEquals(ErrorCode.AUTHENTICATION_MISMATCH_EXCEPTION.getMessage(), ex.getMessage());
  }
}
