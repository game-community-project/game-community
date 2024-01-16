package com.gamecommunity.domain.guestbook.entity;

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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@Table(name = "guest_book")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Guestbook extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 5000, nullable = false)
  private String content;

  @JoinColumn(name = "to_user_id")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private User toUser; // 방명록 주인

  @JoinColumn(name = "from_user_id")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private User fromUser; // 방명록 작성자


}
