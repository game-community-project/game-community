package com.gamecommunity.domain.team.repository;

import com.gamecommunity.domain.team.entity.Team;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

  Team findByTeamName(String teamName);

  Optional<Team> findByTeamId(Long teamId);
}

