package com.gamecommunity.domain.admin.dto;

import java.time.LocalDateTime;

public record UserBlockRequestDto(

    long userId,
    LocalDateTime blockDate

) {

}
