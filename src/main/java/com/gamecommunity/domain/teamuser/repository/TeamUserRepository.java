package com.gamecommunity.domain.teamuser.repository;

import com.gamecommunity.domain.team.entity.Team;
import com.gamecommunity.domain.teamuser.entity.TeamUser;
import com.gamecommunity.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamUserRepository extends JpaRepository<TeamUser, Long> {
  Page<TeamUser> findAllByUserId(Long userId, Pageable pageable);

  Optional<TeamUser> findByTeamAndUser(Team team, User deletedUser);
}
