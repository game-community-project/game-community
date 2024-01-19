package com.gamecommunity.domain.team.service;

import com.gamecommunity.domain.comment.dto.CommentResponseDto;
import com.gamecommunity.domain.post.dto.PostResponseDto;
import com.gamecommunity.domain.post.entity.Post;
import com.gamecommunity.domain.team.dto.TeamRequestDto;
import com.gamecommunity.domain.team.dto.TeamResponseDto;
import com.gamecommunity.domain.team.entity.Team;
import com.gamecommunity.domain.team.repository.TeamRepository;
import com.gamecommunity.domain.teamuser.entity.TeamUser;
import com.gamecommunity.domain.teamuser.repository.TeamUserRepository;
import com.gamecommunity.domain.user.entity.User;
import com.gamecommunity.domain.user.repository.UserRepository;
import com.gamecommunity.global.enums.game.name.GameName;
import com.gamecommunity.global.exception.common.BusinessException;
import com.gamecommunity.global.exception.common.ErrorCode;
import com.gamecommunity.global.response.ApiResponse;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

@Service
@AllArgsConstructor
public class TeamService {

  private final TeamRepository teamRepository;
  private final TeamUserRepository teamUserRepository;
  private final UserRepository userRepository;

  public void createTeam(User user, TeamRequestDto teamRequestDto) {
    Team team = new Team(user.getId(), teamRequestDto);
    teamRepository.save(team);
    addAdminUserToTeam(user, team);
  }

  public Page<TeamResponseDto> getTeamsByGameName(
      int page, int size, String sortKey, boolean isAsc, GameName gameName) {

    Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
    Sort sort = Sort.by(direction, sortKey);
    Pageable pageable = PageRequest.of(page, size, sort);

    for (GameName game : GameName.values()) {
      if (game.equals(gameName)) {
        Page<Team> teamList = teamRepository.findAllByGameName(game, pageable);
        return teamList.map(TeamResponseDto::new);
      }
    }

    throw new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_GAME);
  }


  public Page<TeamResponseDto> getTeamsByUser(int page, int size, String sortKey, boolean isAsc,
      User user) {
    Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
    Sort sort = Sort.by(direction, sortKey);
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<TeamUser> teamListUser = teamUserRepository.findAllByUserId(user.getId(), pageable);
    return teamListUser.map(TeamResponseDto::new);
  }

  public void deleteTeam(User user, Long teamId) {

    Team team = teamRepository.findByTeamId(teamId).orElseThrow(() ->
        new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_TEAM_EXCEPTION));

    if (!user.getId().equals(team.getTeamAdminId())) {
      throw new BusinessException(HttpStatus.BAD_REQUEST,
          ErrorCode.AUTHENTICATION_MISMATCH_EXCEPTION);
    }

    teamRepository.delete(team);
  }

  @Transactional
  public void updateTeam(User user, Long teamId, TeamRequestDto teamRequestDto) {

    Team team = teamRepository.findByTeamId(teamId).orElseThrow(() ->
        new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_TEAM_EXCEPTION));

    if (!user.getId().equals(team.getTeamAdminId())) {
      throw new BusinessException(HttpStatus.BAD_REQUEST,
          ErrorCode.AUTHENTICATION_MISMATCH_EXCEPTION);
    }

    team.update(teamRequestDto);
  }

  public void addUserToTeam(User user, Long teamId, Long invitedUserId) {

    Team team = teamRepository.findByTeamId(teamId).orElseThrow(() ->
        new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_TEAM_EXCEPTION));

    if (!user.getId().equals(team.getTeamAdminId())) {
      throw new BusinessException(HttpStatus.BAD_REQUEST,
          ErrorCode.AUTHENTICATION_MISMATCH_EXCEPTION);
    }

    User invitedUser = userRepository.findById(invitedUserId).orElseThrow(() ->
        new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_USER_EXCEPTION));

    TeamUser teamUser = new TeamUser(team, invitedUser);

    teamUserRepository.save(teamUser);
  }

  public void addAdminUserToTeam(User user, Team team) {
    TeamUser teamUser = new TeamUser(team, user);
    teamUserRepository.save(teamUser);
  }

  public void deleteUserFromTeam(User user, Long teamId, Long userId) {

    Team team = teamRepository.findByTeamId(teamId).orElseThrow(() ->
        new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_TEAM_EXCEPTION));

    if (!user.getId().equals(team.getTeamAdminId())) {
      throw new BusinessException(HttpStatus.BAD_REQUEST,
          ErrorCode.AUTHENTICATION_MISMATCH_EXCEPTION);
    }

    User deletedUser = userRepository.findById(userId).orElseThrow(() ->
        new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_USER_EXCEPTION));

    TeamUser teamUser = teamUserRepository.findByTeamAndUser(team, deletedUser).orElseThrow(() ->
        new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_TEAM_USER));

    teamUserRepository.delete(teamUser);
  }
}

