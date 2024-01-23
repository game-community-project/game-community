package com.gamecommunity.domain.team.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gamecommunity.domain.team.dto.TeamRequestDto;
import com.gamecommunity.domain.team.dto.TeamResponseDto;
import com.gamecommunity.domain.team.entity.Team;
import com.gamecommunity.domain.team.repository.TeamRepository;
import com.gamecommunity.domain.teamuser.entity.TeamUser;
import com.gamecommunity.domain.teamuser.repository.TeamUserRepository;
import com.gamecommunity.domain.teamuser.service.TeamUserService;
import com.gamecommunity.domain.test.TeamTestHelper;
import com.gamecommunity.domain.test.TeamUserTest;
import com.gamecommunity.domain.user.entity.User;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
  @Mock
  private TeamUserService teamUserService;


  @Test
  @DisplayName("팀 생성 - 성공")
  void createTeamTestSuccess() {

    // given
    TeamRequestDto teamRequestDto = new TeamRequestDto(TEST_TEAM_NAME,
        TEST_TEAM_INTRODUCTION);
    User loginUser = TEST_USER;

    // when
    teamService.createTeam(loginUser, teamRequestDto);

    // Then
    verify(teamRepository, times(1)).save(any(Team.class));
  }


  @Test
  public void testGetTeamsByUser() {

    // given
    int page = 0;
    int size = 10;
    String sortKey = "teamName";
    boolean isAsc = true;
    User user = TEST_USER;

    List<TeamUser> fakeTeamUsers = Arrays.asList(
        TeamTestHelper.createFakeTeamUser(
            TeamTestHelper.createFakeTeam(1L, 1L, "Team1", "Introduction1"
            ), user),
        TeamTestHelper.createFakeTeamUser(
            TeamTestHelper.createFakeTeam(2L, 2L, "Team2", "Introduction2"
            ), user),
        TeamTestHelper.createFakeTeamUser(
            TeamTestHelper.createFakeTeam(3L, 3L, "Team3", "Introduction3"
            ), user)
    );
    when(teamUserRepository.findAllByUser(eq(user), any())).thenReturn(
        new PageImpl<>(fakeTeamUsers));

    // when
    Page<TeamResponseDto> result = teamService.getTeamsByUser(page, size, sortKey, isAsc, user);

    // then
    assertEquals(3, result.getTotalElements());
    verify(teamUserRepository, times(1)).findAllByUser(eq(user), any());
  }


  @Test
  @DisplayName("팀 정보 수정  - 성공")
  void updateTeamTestSuccess() {

    // given
    TeamRequestDto teamRequestDto = new TeamRequestDto(TEST_ANOTHER_TEAM_NAME,
        TEST_ANOTHER_TEAM_INTRODUCTION);
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
        TEST_ANOTHER_TEAM_INTRODUCTION);
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
        TEST_ANOTHER_TEAM_INTRODUCTION);
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
