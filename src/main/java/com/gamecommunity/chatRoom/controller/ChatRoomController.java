package com.gamecommunity.chatRoom.controller;

import com.gamecommunity.chatRoom.service.ChatRoomService;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/chat")
@AllArgsConstructor
public class ChatRoomController {

  private final ChatRoomService chatRoomService;

  // 게시글 생성 시 채팅방 생성
  @PostMapping("/chat/posts/{postId}/create")
  public void createChatRoom(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    chatRoomService.createChatRoom(postId, userDetails);
  }

}
