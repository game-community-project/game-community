package com.gamecommunity.domain.user.repository;


import com.gamecommunity.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

  Optional<User> findByEmail(String Email);
  Optional<User> findById(Long userId);

  Optional<User> findByNickname(String nickname);

  Optional<User> findByKakaoId(Long kakaoId);


}
