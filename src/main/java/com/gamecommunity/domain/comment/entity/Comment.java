package com.gamecommunity.domain.comment.entity;


import com.gamecommunity.domain.comment.dto.CommentRequestDto;
import com.gamecommunity.domain.post.entity.Post;
import com.gamecommunity.domain.user.entity.User;
import com.gamecommunity.global.auditing.TimeStamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "comments")
@Builder
@AllArgsConstructor
public class Comment extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long commentId;

  @Column(nullable = false)
  private Long ref;

  @Column(nullable = false)
  private Long level;

  @Column(nullable = false)
  private Long refOrder;

  @Column(nullable = false)
  private Long childCount;

  @Column(nullable = false)
  private Long parentId;

  @Column(nullable = false)
  private String content;

  @Column(nullable = false)
  private Boolean accept;

  @Column
  private Boolean isDeleted = false;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne
  @JoinColumn(name = "post_id", nullable = false)
  private Post post;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_comment_id")
  private Comment parentComment; // 대댓글이면 해당 부모 댓글에 대한 참조

  public Comment(Long ref, Long level, Long refOrder, Long childCount, Long parentId, User user,
      Post post, CommentRequestDto requestDto, Comment parentComment) {
    this.ref = ref;
    this.level = level;
    this.refOrder = refOrder;
    this.childCount = childCount;
    this.parentId = parentId;
    this.user = user;
    this.post = post;
    this.content = requestDto.content();
    this.accept = false;
    this.parentComment = parentComment; // 부모 댓글 설정
    this.isDeleted = false;
  }

  public void update(CommentRequestDto requestDto) {
    this.content = requestDto.content();
  }

  public void toggleDeleted(){
    this.isDeleted = true;
  }

  public void setAccepted(boolean accept) {
    this.accept = accept;
  }

  public void decrementChildCount() {
    this.childCount -= 1;
  }


}
