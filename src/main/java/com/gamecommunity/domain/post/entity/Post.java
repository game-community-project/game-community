package com.gamecommunity.domain.post.entity;

import com.gamecommunity.global.auditing.TimeStamped;
import com.gamecommunity.global.enums.board.BoardName;
import com.gamecommunity.global.enums.game.name.GameName;
import com.gamecommunity.global.enums.game.type.GameType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "posts")
public class Post extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long postId;

  @Column(nullable = false)
  private String postTitle;

  @Column(nullable = false)
  private String postContent;

  @Column
  private String postImageUrl;

  @Column(nullable = false)
  private String postAuthor;

  @Column
  @Enumerated(EnumType.STRING)
  private GameType gameType;

  @Column
  @Enumerated(EnumType.STRING)
  private GameName gameName;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private BoardName boardName;

  @Column(nullable = false)
  private Integer report;

  @Column(nullable = false)
  private Integer postLike;

  @Column(nullable = false)
  private Integer postUnlike;

}
