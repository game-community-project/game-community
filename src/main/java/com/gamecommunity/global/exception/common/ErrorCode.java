package com.gamecommunity.global.exception.common;

import lombok.Getter;

@Getter
public enum ErrorCode {

  // post
  NOT_FOUND_POST_EXCEPTION("해당 게시글을 찾을 수 없습니다.");

  private final String message;

  ErrorCode(String message) {
    this.message = message;
  }

}

