package com.gamecommunity.domain.guestbook.dto;

import com.gamecommunity.domain.guestbook.entity.Guestbook;
import lombok.Builder;

@Builder
public record GuestbookDto(

        String nickname,
        String content

) {

  public GuestbookDto(Guestbook guestbook) {
    this(guestbook.getFromUser().getNickname(),
            guestbook.getContent());
  }

}
