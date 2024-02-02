package com.gamecommunity.chatRoom.repository;

import com.gamecommunity.chatRoom.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<ChatMessage, Long> {

}
