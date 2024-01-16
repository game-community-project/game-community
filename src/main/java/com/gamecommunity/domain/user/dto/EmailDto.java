package com.gamecommunity.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class EmailDto{

  public record Request(
      @NotBlank(message = "이메일 공백일 수 없습니다.")
      @Pattern(regexp ="^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", message = "이메일 형식에 맞게 적어 주세요")
      String email){
  }

  public record CheckRequest(
      @NotBlank(message = "이메일 공백일 수 없습니다.")
      @Pattern(regexp ="^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", message = "이메일 형식에 맞게 적어 주세요")
      String email,
      @NotBlank(message = "인증 번호를 입력해 주세요")
      String authNum){
  }

}
