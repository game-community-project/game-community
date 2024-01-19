package com.gamecommunity.domain.post.repository;

import com.gamecommunity.domain.post.entity.ApiPostUseTime;
import com.gamecommunity.global.enums.board.BoardName;
import com.gamecommunity.global.enums.game.name.GameName;
import com.gamecommunity.global.enums.game.type.GameType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiPostUseTimeRepository extends JpaRepository<ApiPostUseTime, Long> {

  Optional<ApiPostUseTime> findByGameTypeAndGameNameAndBoardName(
      GameType type, GameName game, BoardName board);

}
