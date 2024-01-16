package com.gamecommunity.domain.guestbook.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ModifyGuestbookDto(
        @NotBlank @Size(max = 5000) String content
) {

}
