package com.gamecommunity.domain.guestbook.dto;

import com.gamecommunity.domain.guestbook.entity.Guestbook;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record GuestbookDto(
        Long id,
        String nickname,
        String content,
        LocalDateTime createdAt

) {

  public GuestbookDto(Guestbook guestbook) {
    this(
            guestbook.getId(),
            guestbook.getFromUser().getNickname(),
            guestbook.getContent(),
            guestbook.getCreatedAt());
  }

}
