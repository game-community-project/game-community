package com.gamecommunity.domain.guestbook.dto;

import com.gamecommunity.domain.guestbook.entity.Guestbook;
import com.gamecommunity.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateGuestbookDto {

  public record Request(
          @Size(max = 50) String content
  ) {

    public Guestbook toEntity(String content, User toUser, User fromUser) {
      return Guestbook.builder()
              .content(content)
              .toUser(toUser)
              .fromUser(fromUser)
              .build();
    }
  }

}
