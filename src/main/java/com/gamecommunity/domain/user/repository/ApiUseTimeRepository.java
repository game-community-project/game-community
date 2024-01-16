package com.gamecommunity.domain.user.repository;

import com.gamecommunity.domain.user.entity.ApiUseTime;
import com.gamecommunity.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiUseTimeRepository extends JpaRepository<ApiUseTime, Long> {
  Optional<ApiUseTime> findByUser(User user);

}

