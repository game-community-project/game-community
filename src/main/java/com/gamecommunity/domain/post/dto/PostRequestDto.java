package com.gamecommunity.domain.post.dto;

import lombok.Builder;

@Builder
public record PostRequestDto(
    String postTitle,
    String postContent

) {

}
