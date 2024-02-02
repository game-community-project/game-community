package com.gamecommunity.chatRoom.service;

import com.gamecommunity.chatRoom.dto.ChatMessageDto;
import com.gamecommunity.chatRoom.dto.ChatRoomDto;
import com.gamecommunity.chatRoom.entity.ChatMessage;
import com.gamecommunity.chatRoom.entity.ChatRoom;
import com.gamecommunity.chatRoom.entity.ChatUserRoom;
import com.gamecommunity.chatRoom.repository.ChatRepository;
import com.gamecommunity.chatRoom.repository.ChatRoomRepository;
import com.gamecommunity.chatRoom.repository.ChatUserRepository;
import com.gamecommunity.domain.post.entity.Post;
import com.gamecommunity.domain.post.repository.PostRepository;
import com.gamecommunity.domain.user.entity.User;
import com.gamecommunity.domain.user.repository.UserRepository;
import com.gamecommunity.global.exception.common.BusinessException;
import com.gamecommunity.global.exception.common.ErrorCode;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
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
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  // 게시글 작성자와 채팅을 건 사용자 간의 채팅방 생성
  @Transactional
  public Long createChatRoom(Long postId, UserDetailsImpl userDetails) {
    Post post = getFindPost(postId);

    User postAuthor = post.getUser();
    User sender = userDetails.getUser();

    ChatRoom chatRoom = chatRoomRepository.save(
            ChatRoom.builder()
                    .post(post)
                    .chatName(post.getPostTitle())
                    .build()
    );

    // 채팅 참여자 추가 (게시글 작성자 | 채팅을 건 사용자)
    ChatUserRoom postAuthorChatUserRoom = ChatUserRoom.builder()
            .user(postAuthor)
            .chatRooms(chatRoom)
            .build();
    ChatUserRoom currentUserChatUserRoom = ChatUserRoom.builder()
            .user(sender)
            .chatRooms(chatRoom)
            .build();

    chatUserRepository.saveAll(List.of(postAuthorChatUserRoom, currentUserChatUserRoom));

    return chatRoom.getId(); // 채팅방 이동을 위한 id 반환
  }

  // 유저가 속한 채팅방 전체 조회
  public List<ChatRoomDto> getChatRooms(Long userId) {
    List<ChatUserRoom> chatUserRoomList = chatUserRepository.findAllByUserId(userId);

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
  public List<ChatMessageDto> getChatMsg(Long chatRoomId, UserDetailsImpl userDetails) {
    Optional<ChatUserRoom> chatUserRoom = chatUserRepository.findByChatRoomsIdAndUserId(chatRoomId, userDetails.getUser().getId());

    return chatUserRoom.map(userRoom -> userRoom.getChatMessages().stream()
                    .map(chatMessage -> {
                      ChatMessageDto chatMessageDto = new ChatMessageDto();
                      chatMessageDto.setId(chatMessage.getId());
                      chatMessageDto.setNickname(chatMessage.getUser().getNickname());
                      chatMessageDto.setChatContent(chatMessage.getChatContent());
                      chatMessageDto.setCreatedAt(chatMessage.getCreatedAt());
                      return chatMessageDto;
                    })
                    .collect(Collectors.toList()))
            .orElse(Collections.emptyList());
  }

  // 채팅 저장
  public void saveChat(Long chatRoomId, Long userId, ChatMessageDto chatMessageDto) {
    ChatRoom chatRoom = findChatRoom(chatRoomId);
    User user = getUser(userId);

    ChatMessage chat = ChatMessage.builder()
            .user(user)
            .chatRooms(chatRoom)
            .chatContent(chatMessageDto.getChatContent())
            .build();

    chatRepository.save(chat);
  }

  // 채팅방 나가기
  public void leaveChatRoom(Long chatRoomId, UserDetailsImpl userDetails) {

    ChatUserRoom chatUserRoom = chatUserRepository.findByChatRoomsIdAndUserId(chatRoomId,
                    userDetails.getUser().getId())
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

  public Post getFindPost(Long postId) {
    return postRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                    ErrorCode.NOT_FOUND_POST_EXCEPTION));
  }

}
