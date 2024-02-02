package com.gamecommunity.chatRoom.repository;

import com.gamecommunity.chatRoom.entity.ChatUserRoom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatUserRepository extends JpaRepository<ChatUserRoom, Long> {

  List<ChatUserRoom> findAllByUserId(Long userId);

}
