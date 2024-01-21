package com.gamecommunity.domain.comment.dto;

import com.gamecommunity.domain.comment.entity.Comment;
import java.time.LocalDateTime;

public record CommentResponseDto(
    Long commentId,
    String author,
    String content,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt
) {

  public CommentResponseDto(Comment comment) {
    this(comment.getCommentId(),
        comment.getUser().getNickname(),
        comment.getContent(),
        comment.getCreatedAt(),
        comment.getModifiedAt());
  }
}
