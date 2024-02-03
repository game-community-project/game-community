package com.gamecommunity.chatRoom.repository;

import com.gamecommunity.chatRoom.entity.ChatRoom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {


}
