package com.gamecommunity.chatRoom.entity;

import com.gamecommunity.domain.user.entity.User;
import com.gamecommunity.global.auditing.TimeStamped;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatUserRoom extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "first_user_id")
  private User firstUser; // 채팅 참여자와 연결된 첫 번째 참여자

  @ManyToOne
  @JoinColumn(name = "second_user_id")
  private User secondUser; // 채팅 참여자와 연결된 두 번째 참여자

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "chatRoom_id")
  private ChatRoom chatRooms; // 채팅 참여자와 연결된 채팅방

  @OneToMany(mappedBy = "chatUserRoom", cascade = CascadeType.ALL)
  private List<ChatMessage> chatMessages;

  public ChatUserRoom(User firstUser, User secondUser) {
    this.firstUser = firstUser;
    this.secondUser = secondUser;
  }

}
