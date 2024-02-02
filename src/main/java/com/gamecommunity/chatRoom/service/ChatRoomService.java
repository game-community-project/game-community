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
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;
  private final ChatUserRepository chatUserRepository;
  private final ChatRepository chatRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  // 게시글 생성 시 채팅방 생성
  public void createChatRoom(Long postId, UserDetailsImpl userDetails) {
    Post post = getFindPost(postId);

    ChatRoom chatRoom = ChatRoom.builder()
            .post(post)
            .chatName(post.getPostTitle())
            .activated(true)
            .build();

    chatRoomRepository.save(chatRoom);

    ChatUserRoom chatUserRoom = ChatUserRoom.builder()
            .user(userDetails.getUser())
            .chatRooms(chatRoom)
            .build();

    chatUserRepository.save(chatUserRoom);
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
