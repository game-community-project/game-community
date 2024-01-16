package com.gamecommunity.domain.post.repository;


import com.gamecommunity.domain.post.entity.Post;
import com.gamecommunity.global.enums.board.BoardName;
import com.gamecommunity.global.enums.game.name.GameName;
import com.gamecommunity.global.enums.game.type.GameType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

  Page<Post> findByGameTypeAndGameNameAndBoardName(
      GameType type, GameName game, BoardName board, Pageable pageable);
}
