package com.gamecommunity.domain.post.dto;

import com.gamecommunity.global.enums.board.BoardName;
import com.gamecommunity.global.enums.game.name.GameName;
import com.gamecommunity.global.enums.game.type.GameType;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record PostResponseDto(

    Long postId,
    String postTitle,
    String postContent,
    String postAuthor,
    String postImageUrl,
    GameType gameType,
    GameName gameName,
    BoardName boardName,
    Integer report,
    Integer postLike,
    Integer postUnlike,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt) {

}
