package com.gamecommunity.domain.guestbook.dto;

import com.gamecommunity.domain.guestbook.entity.Guestbook;
import com.gamecommunity.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record GuestbookDto(

        List<GuestbookDto.GuestbookLisstDto> guestbookList

) {


  public static GuestbookDto from(User toUser) {

    return GuestbookDto.builder()
            .guestbookList(toUser.getGuestbookList().stream().map(GuestbookDto.GuestbookLisstDto::from).toList())
            .build();
  }

  @Builder
  private record GuestbookLisstDto(String nickname,String content, LocalDateTime createdAt) {

    public static GuestbookDto.GuestbookLisstDto from(Guestbook guestbook) {

      return GuestbookDto.GuestbookLisstDto.builder()
              .nickname(guestbook.getFromUser().getNickname())
              .content(guestbook.getContent())
              .createdAt(guestbook.getCreatedAt())
              .build();
    }
  }

}
