package com.gamecommunity.chatRoom.repository;

import com.gamecommunity.chatRoom.entity.ChatUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {


}
