package com.gamecommunity.chatRoom.controller;

import com.gamecommunity.chatRoom.dto.ChatMessageDto;
import com.gamecommunity.chatRoom.dto.ChatRoomDto;
import com.gamecommunity.chatRoom.service.ChatRoomService;
import com.gamecommunity.global.response.ApiResponse;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/chat")
@AllArgsConstructor
public class ChatRoomController {

  private final ChatRoomService chatRoomService;

  // 채팅방 생성
  @PostMapping("/createRoom/{secondUserId}")
  public ResponseEntity<ApiResponse<Long>> createChatRoom(
          @AuthenticationPrincipal UserDetailsImpl userDetails,
          @PathVariable Long secondUserId

  ) {
    Long chatRoomId = chatRoomService.createChatRoom(userDetails, secondUserId);
    return ResponseEntity.ok(ApiResponse.ok("채팅방 생성 성공", chatRoomId));
  }

  // 유저가 속한 채팅방 전체 조회
  @GetMapping("/list")
  public ResponseEntity<ApiResponse> getChatRooms(
          @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    List<ChatRoomDto> chatRooms = chatRoomService.getChatRooms(userDetails);
    return ResponseEntity.ok(ApiResponse.ok("채팅방 전체 조회 성공", chatRooms));
  }

  // 특정 채팅방 조회
  @GetMapping("/{chatRoomId}")
  public ResponseEntity<ApiResponse<ChatRoomDto>> getChatRoom(
          @PathVariable Long chatRoomId
  ) {
    ChatRoomDto chatRoomDto = chatRoomService.getChatRoom(chatRoomId);
    return ResponseEntity.ok(ApiResponse.ok("특정 채팅방 조회 성공", chatRoomDto)); // 수정 예정
  }

  // 특정 채팅방의 메세지 조회
  @GetMapping("/{chatRoomId}/messages")
  public ResponseEntity<ApiResponse<List<ChatMessageDto>>> getChatMsg(
          @PathVariable Long chatRoomId
  ) {
    List<ChatMessageDto> chatMsg = chatRoomService.getChatMsg(chatRoomId);
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
  @DeleteMapping("/leave/{chatRoomId}")
  public ResponseEntity<ApiResponse> leaveChatRoom(
          @PathVariable Long chatRoomId,
          @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    chatRoomService.leaveChatRoom(chatRoomId, userDetails);
    return ResponseEntity.ok(ApiResponse.ok("채팅방 나가기", null));
  }

}
