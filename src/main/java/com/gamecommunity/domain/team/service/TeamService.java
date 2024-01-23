package com.gamecommunity.domain.team.service;

import com.gamecommunity.domain.team.dto.TeamRequestDto;
import com.gamecommunity.domain.team.dto.TeamResponseDto;
import com.gamecommunity.domain.team.entity.Team;
import com.gamecommunity.domain.team.repository.TeamRepository;
import com.gamecommunity.domain.teamuser.entity.TeamUser;
import com.gamecommunity.domain.teamuser.repository.TeamUserRepository;
import com.gamecommunity.domain.teamuser.service.TeamUserService;
import com.gamecommunity.domain.user.entity.User;
import com.gamecommunity.global.exception.common.BusinessException;
import com.gamecommunity.global.exception.common.ErrorCode;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TeamService {

  private final TeamRepository teamRepository;
  private final TeamUserRepository teamUserRepository;
  private final TeamUserService teamUserService;

  public TeamResponseDto createTeam(User user, TeamRequestDto teamRequestDto) {
    Team team = new Team(user.getId(), teamRequestDto);
    teamRepository.save(team);
    teamUserService.addAdminUserToTeam(user, team);
    return new TeamResponseDto(team);
  }

  public TeamResponseDto getTeam(Long teamId) {

    Team team = teamRepository.findByTeamId(teamId).orElseThrow(() ->
        new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_TEAM_EXCEPTION));

    return new TeamResponseDto(team);
  }

  public List<String> getUsersFromTeam(Long teamId) {
    Team team = teamRepository.findByTeamId(teamId).orElseThrow(() ->
        new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_TEAM_EXCEPTION));
    List<String> nicknameList = team.getTeamUsers().stream().map(h -> h.getUser().getNickname())
        .toList();
    return nicknameList;
  }

  public Page<TeamResponseDto> getTeamsByUser(int page, int size, String sortKey, boolean isAsc,
      User user) {
    Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
    Sort sort = Sort.by(direction, sortKey);
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<TeamUser> teamListUser = teamUserRepository.findAllByUser(user, pageable);

    Page<TeamResponseDto> teamResponsePage = teamListUser.map(
        teamUser -> new TeamResponseDto(teamUser.getTeam()));

    return teamResponsePage;
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

  public Boolean isTeamAdmin(User user, Long teamId) {
    Team team = teamRepository.findByTeamId(teamId).orElseThrow(() ->
        new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_TEAM_EXCEPTION));

    return team.getTeamAdminId().equals(user.getId());
  }
}

