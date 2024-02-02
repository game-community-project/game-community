package com.gamecommunity.chatRoom.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageDto {
    private Long id;
    private String nickname;
    private String chatContent;
    private LocalDateTime createdAt;
  }


