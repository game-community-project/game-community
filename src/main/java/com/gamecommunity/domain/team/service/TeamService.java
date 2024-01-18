package com.gamecommunity.domain.team.service;

import com.gamecommunity.domain.team.dto.TeamRequestDto;
import com.gamecommunity.domain.team.dto.TeamResponseDto;
import com.gamecommunity.domain.team.entity.Team;
import com.gamecommunity.domain.team.repository.TeamRepository;
import com.gamecommunity.domain.teamuser.repository.TeamUserRepository;
import com.gamecommunity.domain.user.entity.User;
import com.gamecommunity.domain.user.repository.UserRepository;
import com.gamecommunity.global.enums.game.name.GameName;
import com.gamecommunity.global.exception.common.BusinessException;
import com.gamecommunity.global.exception.common.ErrorCode;
import com.gamecommunity.global.response.ApiResponse;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
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
    //addAdminUserToTeam(user,team);
  }

  public Map<GameName, List<TeamResponseDto>> getTeamsByGame() {
    Map<GameName, List<TeamResponseDto>> teamMap = new HashMap<>();
    for (GameName game : GameName.values()) {
      List<TeamResponseDto> teamResponseDtos = teamRepository.findAllByGameName(game).stream()
          .map(TeamResponseDto::new).toList();
      teamMap.put(game, teamResponseDtos);
    }
    return teamMap;
  }

  public List<TeamResponseDto> getTeamsByUser(User user) {
    List<TeamResponseDto> teamResponseDtos = teamUserRepository.findAllByUserId(user.getId())
        .stream() // TeamUser Entity가 리스트로 나옴
        .map(Team -> new TeamResponseDto(Team.getTeam())).toList();

    return teamResponseDtos;
  }

  public void deleteTeam(User user, Long teamId) {

    Team team = teamRepository.findByTeamId(teamId).orElseThrow( () ->
        new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_TEAM_EXCEPTION));

    if(!user.getId().equals(team.getTeamAdminId())){
      throw new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.AUTHENTICATION_MISMATCH_EXCEPTION);
    }

    teamRepository.delete(team);
  }

  @Transactional
  public void updateTeam(User user, Long teamId, TeamRequestDto teamRequestDto) {

    Team team = teamRepository.findByTeamId(teamId).orElseThrow( () ->
        new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_TEAM_EXCEPTION));

    if(!user.getId().equals(team.getTeamAdminId())){
      throw new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.AUTHENTICATION_MISMATCH_EXCEPTION);
    }

    team.update(teamRequestDto);
  }
}
