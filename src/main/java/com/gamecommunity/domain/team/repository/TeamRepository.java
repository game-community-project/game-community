package com.gamecommunity.domain.team.repository;

import com.gamecommunity.domain.team.entity.Team;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

  Team findByTeamName(String teamName);

  Optional<Team> findByTeamId(Long teamId);

  @Nonnull
  Page<Team> findAll(@Nonnull Pageable pageable);
}

