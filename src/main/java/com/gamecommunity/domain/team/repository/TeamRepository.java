package com.gamecommunity.domain.team.repository;

import com.gamecommunity.domain.team.entity.Team;
import com.gamecommunity.global.enums.game.name.GameName;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

  List<Team> findAllByGameName(GameName gameName);

  Team findByTeamName(String teamName);

  Optional<Team> findByTeamId(Long teamId);
}

