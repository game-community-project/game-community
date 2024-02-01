package com.gamecommunity.domain.comment.dto;

import com.gamecommunity.domain.comment.controller.CommentController;
import com.gamecommunity.domain.comment.entity.Comment;
import java.time.LocalDateTime;
import net.minidev.json.JSONUtil;

public record CommentResponseDto(
    Long commentId,
    String parentAuthor,
    String author,
    String content,
    Boolean accept,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt
) {

  public CommentResponseDto(Comment comment) {
    this(comment.getCommentId(),
        getAuthorFromParent(comment),
        comment.getUser().getNickname(),
        getContentIfNotDeleted(comment),
        comment.getAccept(),
        comment.getCreatedAt(),
        comment.getModifiedAt());
  }

  private static String getAuthorFromParent(Comment comment) {
    if (comment.getParentComment() != null && comment.getParentComment().getUser() != null) {
      return comment.getParentComment().getUser().getNickname();
    } else {
      return null;
    }
  }

  private static String getContentIfNotDeleted(Comment comment){
    if(comment.getIsDeleted()){
      return "삭제된 댓글입니다.";
    } else {
      return comment.getContent();
    }
  }
}