package com.gamecommunity.domain.guestbook.repository;

import com.gamecommunity.domain.guestbook.entity.Guestbook;
import com.gamecommunity.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestbookRepository extends JpaRepository<Guestbook, Long> {

  Page<Guestbook> findAllByToUser(User toUser, Pageable pageable);

}
