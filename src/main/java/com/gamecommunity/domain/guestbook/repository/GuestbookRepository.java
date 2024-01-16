package com.gamecommunity.domain.guestbook.repository;

import com.gamecommunity.domain.guestbook.entity.Guestbook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestbookRepository extends JpaRepository<Guestbook, Long> {

}
