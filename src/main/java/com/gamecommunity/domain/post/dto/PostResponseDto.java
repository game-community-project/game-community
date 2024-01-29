package com.gamecommunity.domain.post.dto;

import com.gamecommunity.domain.post.entity.Post;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record PostResponseDto(

    Long postId,
    String postTitle,
    String postContent,
    String postAuthor,
    String postImageUrl,
    Integer report,
    Integer postLike,
    Integer postUnlike,
    Boolean close,
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
        post.getReport(),
        post.getPostLike(),
        post.getPostUnlike(),
        post.getClose(),
        post.getCreatedAt(),
        post.getModifiedAt(),
        post.getUser().getId()
    );
  }
}
