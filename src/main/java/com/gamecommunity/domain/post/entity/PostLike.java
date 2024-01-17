package com.gamecommunity.domain.post.entity;

import com.gamecommunity.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "post_likes")
public class PostLike {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long postLikeId;

  @Column
  private Boolean islike;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", nullable = false)
  private Post post;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  public static PostLike fromUserAndPost(User loginUser, Boolean isLike, Post post) {
    return PostLike.builder()
        .user(loginUser)
        .islike(isLike)
        .post(post)
        .build();
  }
}
