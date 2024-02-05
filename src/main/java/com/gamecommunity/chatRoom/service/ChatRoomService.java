package com.gamecommunity.chatRoom.service;

import com.gamecommunity.chatRoom.dto.ChatMessageDto;
import com.gamecommunity.chatRoom.dto.ChatRoomDto;
import com.gamecommunity.chatRoom.entity.ChatMessage;
import com.gamecommunity.chatRoom.entity.ChatRoom;
import com.gamecommunity.chatRoom.entity.ChatUserRoom;
import com.gamecommunity.chatRoom.repository.ChatRepository;
import com.gamecommunity.chatRoom.repository.ChatRoomRepository;
import com.gamecommunity.chatRoom.repository.ChatUserRepository;
import com.gamecommunity.domain.user.entity.User;
import com.gamecommunity.domain.user.repository.UserRepository;
import com.gamecommunity.global.exception.common.BusinessException;
import com.gamecommunity.global.exception.common.ErrorCode;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;
  private final ChatUserRepository chatUserRepository;
  private final ChatRepository chatRepository;
  private final UserRepository userRepository;

  // 채팅방 생성
  @Transactional
  public Long createChatRoom(UserDetailsImpl userDetails, Long secondUserId) {
    User firstUser = userDetails.getUser();
    User secondUser = getUser(secondUserId);

    // 두 유저 간의 채팅방 존재 여부 확인
    Optional<ChatUserRoom> existingChatUserRoom = chatUserRepository
            .findChatUserRoomByFirstUserAndSecondUser(firstUser, secondUser);

    ChatRoom chatRoom = existingChatUserRoom.map(ChatUserRoom::getChatRooms)
            .orElseGet(() -> {
              ChatRoom newChatRoom = chatRoomRepository.save(
                      ChatRoom.builder()
                              .chatName(firstUser.getNickname() + "님과 " + secondUser.getNickname() + "님의 채팅방 입니다.")
                              .activated(true)
                              .build()
              );
              ChatUserRoom firstUserChatUserRoom = new ChatUserRoom(firstUser, secondUser);
              firstUserChatUserRoom.setChatRooms(newChatRoom);
              ChatUserRoom secondUserChatUserRoom = new ChatUserRoom(secondUser, firstUser);
              secondUserChatUserRoom.setChatRooms(newChatRoom);
              chatUserRepository.saveAll(List.of(firstUserChatUserRoom, secondUserChatUserRoom));
              return newChatRoom;
            });
    return chatRoom.getId();
  }

  // 유저가 속한 채팅방 전체 조회
  public List<ChatRoomDto> getChatRooms(UserDetailsImpl userDetails) {
    List<ChatUserRoom> chatUserRoomList = chatUserRepository.findAllByFirstUser(
            userDetails.getUser());

    return chatUserRoomList.stream()
            .map(chatUserRoom -> {
              ChatRoom chatRoom = chatUserRoom.getChatRooms();
              ChatRoomDto chatRoomDto = new ChatRoomDto();

              chatRoomDto.setId(chatRoom.getId());
              chatRoomDto.setChatName(chatRoom.getChatName());
              chatRoomDto.setCreatedAt(chatRoom.getCreatedAt());

              return chatRoomDto;
            })
            .collect(Collectors.toList());
  }

  // 특정 채팅방 조회
  public ChatRoomDto getChatRoom(Long chatRoomId) {
    ChatRoom chatRoom = findChatRoom(chatRoomId);

    ChatRoomDto chatRoomDto = new ChatRoomDto();
    chatRoomDto.setId(chatRoom.getId());
    chatRoomDto.setChatName(chatRoom.getChatName());
    chatRoomDto.setCreatedAt(chatRoom.getCreatedAt());

    return chatRoomDto;
  }

  // 특정 채팅방의 메세지 조회
  public List<ChatMessageDto> getChatMsg(Long chatRoomId) {
    List<ChatUserRoom> chatUserRooms = chatUserRepository.findByChatRoomsId(chatRoomId);

    return chatUserRooms.stream()
            .flatMap(userRoom -> userRoom.getChatMessages().stream()
                    .map(chatMessage -> {
                      ChatMessageDto chatMessageDto = new ChatMessageDto();
                      chatMessageDto.setId(chatMessage.getId());
                      chatMessageDto.setNickname(chatMessage.getUser().getNickname());
                      chatMessageDto.setChatContent(chatMessage.getChatContent());
                      chatMessageDto.setCreatedAt(chatMessage.getCreatedAt());
                      return chatMessageDto;
                    }))
            .sorted(Comparator.comparing(ChatMessageDto::getCreatedAt))
            .collect(Collectors.toList());
  }

  // 채팅 저장
  @Transactional
  public void saveChat(Long chatRoomId, Long userId, ChatMessageDto chatMessageDto) {
    ChatRoom chatRoom = findChatRoom(chatRoomId);
    User user = getUser(userId);

    ChatUserRoom chatUserRoom = chatUserRepository.findByChatRoomsIdAndFirstUser(chatRoomId, user)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_CHATROOM_MEMBER_EXCEPTION));

    ChatMessage chat = ChatMessage.builder()
            .user(user)
            .chatUserRoom(chatUserRoom)
            .chatContent(chatMessageDto.getChatContent())
            .build();

    chatRepository.save(chat);
  }

  // 채팅방 나가기
  @Transactional
  public void leaveChatRoom(Long chatRoomId, UserDetailsImpl userDetails) {

    ChatUserRoom chatUserRoom = chatUserRepository.findByChatRoomsIdAndFirstUser(chatRoomId,
                    userDetails.getUser())
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                    ErrorCode.NOT_FOUND_CHATROOM_MEMBER_EXCEPTION));

    chatUserRepository.delete(chatUserRoom);
  }

  public ChatRoom findChatRoom(Long chatRoomId) {
    return chatRoomRepository.findById(chatRoomId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                    ErrorCode.NOT_FOUND_CHATROOM_EXCEPTION));
  }

  public User getUser(Long userId) {
    return userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                    ErrorCode.NOT_FOUND_USER_EXCEPTION));
  }

}
