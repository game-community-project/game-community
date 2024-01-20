package com.gamecommunity.domain.post.dto;

import com.gamecommunity.domain.post.entity.Post;
import com.gamecommunity.domain.user.entity.User;
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
    LocalDateTime modifiedAt,
    Long userId
    ) {

  public static PostResponseDto fromEntity(Post post) {
    return new PostResponseDto(
        post.getPostId(),
        post.getPostTitle(),
        post.getPostContent(),
        post.getPostAuthor(),
        post.getPostImageUrl(),
        post.getGameType(),
        post.getGameName(),
        post.getBoardName(),
        post.getReport(),
        post.getPostLike(),
        post.getPostUnlike(),
        post.getCreatedAt(),
        post.getModifiedAt(),
        post.getUser().getId()
    );
  }
}
