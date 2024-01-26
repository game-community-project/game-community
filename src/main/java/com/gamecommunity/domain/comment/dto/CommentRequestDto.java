package com.gamecommunity.domain.comment.dto;


import com.gamecommunity.domain.comment.entity.Comment;

public record CommentRequestDto(
    Long parentId,
    String content
) {

  public CommentRequestDto(Comment comment) {
    this(comment.getParentId(),
        comment.getContent());
  }
}
