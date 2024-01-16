package com.gamecommunity.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
public record LoginRequestDto(
    @NotBlank(message = "이메일 공백일 수 없습니다.")
    String email,
    @NotBlank(message = "비밀번호 공백일 수 없습니다.")
    String password){

}
