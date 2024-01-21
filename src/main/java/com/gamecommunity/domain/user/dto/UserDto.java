package com.gamecommunity.domain.user.dto;

import com.gamecommunity.domain.guestbook.entity.Guestbook;
import com.gamecommunity.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record UserDto (


  Long userId,
  List<GuestbookDto> guestbookList

) {

    public static UserDto from(User user) {

      return UserDto.builder()
              .userId(user.getId())
              .guestbookList(user.getGuestbookList().stream().map(GuestbookDto::from).toList())
              .build();
    }

    @Builder
    private record GuestbookDto(Long id, String content, String nickname, LocalDateTime createdAt) {

      public static UserDto.GuestbookDto from(Guestbook guestbook) {

        return UserDto.GuestbookDto.builder()
                .id(guestbook.getId())
                .content(guestbook.getContent())
                .nickname(guestbook.getFromUser().getNickname())
                .createdAt(guestbook.getCreatedAt())
                .build();
      }
    }

}
