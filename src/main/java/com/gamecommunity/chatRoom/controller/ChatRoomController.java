package com.gamecommunity.chatRoom.controller;

import com.gamecommunity.chatRoom.dto.ChatMessageDto;
import com.gamecommunity.chatRoom.dto.ChatRoomDto;
import com.gamecommunity.chatRoom.entity.ChatRoom;
import com.gamecommunity.chatRoom.service.ChatRoomService;
import com.gamecommunity.global.response.ApiResponse;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/chat")
@AllArgsConstructor
public class ChatRoomController {

  private final ChatRoomService chatRoomService;

  // 게시글 작성자와 채팅을 건 사용자 간의 채팅방 생성
  @PostMapping("/chat/posts/{postId}/create")
  public ResponseEntity<ApiResponse<Long>> createChatRoom(
          @PathVariable Long postId,
          @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    Long chatRoomId = chatRoomService.createChatRoom(postId, userDetails);
    return ResponseEntity.ok(ApiResponse.ok("채팅방 생성 성공", chatRoomId));
  }

  // 유저가 속한 채팅방 전체 조회
  @GetMapping("/chat/users/{userId}")
  public ResponseEntity<ApiResponse> getChatRooms(
          @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    List<ChatRoomDto> chatRooms = chatRoomService.getChatRooms(userDetails.getUser().getId());
    return ResponseEntity.ok(ApiResponse.ok("채팅방 전체 조회 성공", chatRooms));
  }

  // 특정 채팅방 조회
  @GetMapping("/chat/{chatRoomId}")
  public ResponseEntity<ApiResponse<ChatRoomDto>> getChatRoom(
          @PathVariable Long chatRoomId
  ) {
    ChatRoomDto chatRoomDto = chatRoomService.getChatRoom(chatRoomId);
    return ResponseEntity.ok(ApiResponse.ok("특정 채팅방 조회 성공", chatRoomDto)); // 수정 예정
  }

  // 특정 채팅방의 메세지 조회
  @GetMapping("/chat/{chatRoomId}/messages")
  public ResponseEntity<ApiResponse<List<ChatMessageDto>>> getChatMsg(
          @PathVariable Long chatRoomId,
          @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    List<ChatMessageDto> chatMsg = chatRoomService.getChatMsg(chatRoomId, userDetails);
    return ResponseEntity.ok(ApiResponse.ok("특정 채팅방의 메세지 조회 성공", chatMsg));
  }

  // 채팅 저장
  @PostMapping("/saveChat/{chatRoomId}")
  public ResponseEntity<ApiResponse> saveChat(
          @PathVariable Long chatRoomId,
          @AuthenticationPrincipal UserDetailsImpl userDetails,
          @RequestBody ChatMessageDto chatMessageDto
  ) {
    chatRoomService.saveChat(chatRoomId, userDetails.getUser().getId(), chatMessageDto);
    return ResponseEntity.ok(ApiResponse.ok("채팅 저장 성공", null));
  }

  // 채팅방 나가기
  @PostMapping("/leave/{chatRoomId}")
  public ResponseEntity<ApiResponse> leaveChatRoom(
          @PathVariable Long chatRoomId,
          @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    chatRoomService.leaveChatRoom(chatRoomId, userDetails);
    return ResponseEntity.ok(ApiResponse.ok("채팅방 나가기", null));
  }

}
