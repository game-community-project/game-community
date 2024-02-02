package com.gamecommunity.chatRoom.service;

import com.gamecommunity.chatRoom.entity.ChatRoom;
import com.gamecommunity.chatRoom.entity.ChatUser;
import com.gamecommunity.chatRoom.repository.ChatRepository;
import com.gamecommunity.chatRoom.repository.ChatRoomRepository;
import com.gamecommunity.chatRoom.repository.ChatUserRepository;
import com.gamecommunity.domain.post.entity.Post;
import com.gamecommunity.domain.post.repository.PostRepository;
import com.gamecommunity.domain.user.repository.UserRepository;
import com.gamecommunity.global.exception.common.BusinessException;
import com.gamecommunity.global.exception.common.ErrorCode;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
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

  // sender는 임의, 전부 로그인한 유저 (userDetails로 변경 예정)
  // 게시글 생성 시 채팅방 생성
  public void createChatRoom(Long postId, UserDetailsImpl userDetails) {
    Post post = getFindPost(postId);

    ChatRoom chatRoom = ChatRoom.builder()
            .post(post)
            .chatName(post.getPostTitle())
            .activated(true)
            .build();

    chatRoomRepository.save(chatRoom);

    ChatUser chatUser = ChatUser.builder()
            .user(userDetails.getUser())
            .chatRooms(chatRoom)
            .build();

    chatUserRepository.save(chatUser);
  }

  public Post getFindPost(Long postId) {
    return postRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                    ErrorCode.NOT_FOUND_POST_EXCEPTION));
  }

}
