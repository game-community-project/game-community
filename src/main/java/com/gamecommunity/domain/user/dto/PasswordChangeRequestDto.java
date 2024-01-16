package com.gamecommunity.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public record PasswordChangeRequestDto(

    @NotBlank(message = "현재 비밀번호 공백일 수 없습니다.")
    String nowPassword,
    //나중에 비번 정규식 추가
    @NotBlank(message = "신규 비밀번호 공백일 수 없습니다.")
    String newPassword,

    @NotBlank(message = "비밀번호 확인 공백일 수 없습니다.")
    String checkPassword) {

}
