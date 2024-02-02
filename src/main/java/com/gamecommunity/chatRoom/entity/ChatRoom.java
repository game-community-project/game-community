package com.gamecommunity.chatRoom.entity;

import com.gamecommunity.domain.post.entity.Post;
import com.gamecommunity.global.auditing.TimeStamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "chatRoom_id")
  private Long id;

  // 게시글과 연관된 채팅방 생성
  // 게시글과 채팅방을 일대일로 연결
  @OneToOne
  @JoinColumn(name = "post_id")
  private Post post;

  private String chatName; // 채팅방 이름

  @Column(nullable = false)
  private boolean activated;

}
