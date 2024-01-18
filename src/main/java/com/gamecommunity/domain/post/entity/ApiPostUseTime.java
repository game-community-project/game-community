package com.gamecommunity.domain.post.entity;

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
@Table(name = "api_post_use_time")
public class ApiPostUseTime {

  // 게시판별 사용시간 누적 저장
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

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
  private Long totalTime;

  public ApiPostUseTime(GameType type, GameName name, BoardName board, Long totalTime) {
    this.gameType = type;
    this.gameName = name;
    this.boardName = board;
    this.totalTime = totalTime;
  }

  public void addPostUseTime(long useTime) {
    this.totalTime += useTime;
  }

}
