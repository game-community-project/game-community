package com.gamecommunity.domain.user.dto;

import com.gamecommunity.domain.guestbook.entity.Guestbook;
import com.gamecommunity.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record UserProfileDto(

        String nickname,
        String introduction,
        String profile_url,
        List<GuestbookDto> guestbookList

) {

  public static UserProfileDto from(User user) {

    return UserProfileDto.builder()
            .nickname(user.getNickname())
            .introduction(user.getIntroduction())
            .profile_url(user.getProfileUrl())
            .guestbookList(user.getGuestbookList().stream().map(GuestbookDto::from).toList())
            .build();
  }

  @Builder
  private record GuestbookDto(Long id, String content, String nickname, LocalDateTime createdAt) {

    public static GuestbookDto from(Guestbook guestbook) {

      return GuestbookDto.builder()
              .id(guestbook.getId())
              .content(guestbook.getContent())
              .nickname(guestbook.getFromUser().getNickname())
              .createdAt(guestbook.getCreatedAt())
              .build();
    }
  }


}
