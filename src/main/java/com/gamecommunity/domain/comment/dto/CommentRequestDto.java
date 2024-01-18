package com.gamecommunity.domain.comment.dto;


import com.gamecommunity.domain.comment.entity.Comment;

public record CommentRequestDto(
    String content
) {

  public CommentRequestDto(Comment comment) {
    this(comment.getContent());
  }
}

