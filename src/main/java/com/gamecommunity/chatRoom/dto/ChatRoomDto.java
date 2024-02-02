package com.gamecommunity.chatRoom.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomDto {
  private Long id;
  private String chatName;
  private LocalDateTime createdAt;

}
