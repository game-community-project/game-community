package com.gamecommunity.domain.teamuser.service;

import com.gamecommunity.domain.team.entity.Team;
import com.gamecommunity.domain.team.repository.TeamRepository;
import com.gamecommunity.domain.teamuser.entity.TeamUser;
import com.gamecommunity.domain.teamuser.repository.TeamUserRepository;
import com.gamecommunity.domain.user.entity.User;
import com.gamecommunity.domain.user.repository.UserRepository;
import com.gamecommunity.global.exception.common.BusinessException;
import com.gamecommunity.global.exception.common.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TeamUserService {

  private final TeamRepository teamRepository;
  private final TeamUserRepository teamUserRepository;
  private final UserRepository userRepository;

  public void addUserToTeam(User user, Long teamId, String nickname) {

    Team team = teamRepository.findByTeamId(teamId).orElseThrow(() ->
        new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_TEAM_EXCEPTION));

    if (!user.getId().equals(team.getTeamAdminId())) {
      throw new BusinessException(HttpStatus.BAD_REQUEST,
          ErrorCode.AUTHENTICATION_MISMATCH_EXCEPTION);
    }

    User invitedUser = userRepository.findByNickname(nickname).orElseThrow(() ->
        new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_USER_EXCEPTION));

    TeamUser teamUser = new TeamUser(team, invitedUser);

    teamUserRepository.save(teamUser);
  }

  public void addAdminUserToTeam(User user, Team team) {
    TeamUser teamUser = new TeamUser(team, user);
    teamUserRepository.save(teamUser);
  }

  public void deleteUserFromTeam(User user, Long teamId, String nickname) {

    Team team = teamRepository.findByTeamId(teamId).orElseThrow(() ->
        new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_TEAM_EXCEPTION));

    if (!user.getId().equals(team.getTeamAdminId())) {
      throw new BusinessException(HttpStatus.BAD_REQUEST,
          ErrorCode.AUTHENTICATION_MISMATCH_EXCEPTION);
    }

    User deletedUser = userRepository.findByNickname(nickname).orElseThrow(() ->
        new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_USER_EXCEPTION));

    TeamUser teamUser = teamUserRepository.findByTeamAndUser(team, deletedUser).orElseThrow(() ->
        new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_TEAM_USER_EXCEPTION));

    teamUserRepository.delete(teamUser);
  }

  public void leaveFromTeam(User user, Long teamId) {
    Team team = teamRepository.findByTeamId(teamId).orElseThrow(() ->
        new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_TEAM_EXCEPTION));
    TeamUser teamUser = teamUserRepository.findByTeamAndUser(team, user).orElseThrow(() ->
        new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_TEAM_USER_EXCEPTION));
    teamUserRepository.delete(teamUser);
  }
}

