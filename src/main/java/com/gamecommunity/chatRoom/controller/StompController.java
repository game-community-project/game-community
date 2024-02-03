package com.gamecommunity.chatRoom.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StompController {

  private final SimpMessagingTemplate simpMessagingTemplate;

  // 채팅방에 대한 메세지
  @MessageMapping("/chat/send")
  public void sendChatMessage(@Payload Map<String, Object> messageData) {
    System.out.println(messageData); // 받은 메세지
    simpMessagingTemplate.convertAndSend("/queue/" + messageData.get("roomNumber"), messageData);
  }

  // 개인 메세지
  @MessageMapping("/sendToUser/{targetUsername}")
  public void sendPrivateMessage(@DestinationVariable String targetUsername,
          @Payload Map<String, Object> messageData) {
    simpMessagingTemplate.convertAndSend("/queue/" + messageData.get("sender"), messageData);
    System.out.println(targetUsername + ": " + messageData); // 개인 메세지 전송
  }

}
