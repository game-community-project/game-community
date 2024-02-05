package com.gamecommunity.chatRoom.repository;

import com.gamecommunity.chatRoom.entity.ChatUserRoom;
import com.gamecommunity.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatUserRepository extends JpaRepository<ChatUserRoom, Long> {
  Optional<ChatUserRoom> findChatUserRoomByFirstUserAndSecondUser(
          User firstUser, User secondUser);
  List<ChatUserRoom> findAllByFirstUser(User user);
  Optional<ChatUserRoom> findByChatRoomsIdAndFirstUser(Long chatRoomId, User user);

  List<ChatUserRoom> findByChatRoomsId(Long chatRoomId);
}
