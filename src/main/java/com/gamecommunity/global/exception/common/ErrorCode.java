package com.gamecommunity.global.exception.common;

import lombok.Getter;

@Getter
public enum ErrorCode {

  // post
  NOT_FOUND_POST_EXCEPTION("해당 게시글을 찾을 수 없습니다."),
  SELF_LIKE_EXCEPTION("자신의 글에는 좋아요를 누를 수 없습니다."),
  DUPLICATED_LIKE_EXCEPTION("이미 좋아요 또는 싫어요 내역이 있습니다."),
  NOT_FOUND_LIKE_EXCEPTION("좋아요 내역이 없습니다."),
  DUPLICATED_REPORT_EXCEPTION("이미 신고한 내역이 있습니다."),

  // user
  FAILED_ADMIN_PASSWORD_EXCEPTION("관리자 암호 인증 실패해서 가입이 불가능합니다."),
  ALREADY_EXIST_USER_EMAIL_EXCEPTION("이미 존재하는 이메일 입니다."),
  ALREADY_EXIST_USER_NICKNAME_EXCEPTION("이미 존재하는 닉네임 입니다."),
  NOT_EQUALS_CONFIRM_PASSWORD_EXCEPTION("비밀번호 확인이 일치하지 않습니다."),
  FAILED_AUTHENTICATION_EXCEPTION("인증에 실패하였습니다."),
  NOT_FOUND_USER_EXCEPTION("해당 유저는 없습니다."),
  FAILED_EMAIL_SEND_EXCEPTION("이메일 서버 문제 or 잘못된 이메일 주소 입니다"),
  FAILED_EMAIL_AUTHENTICATION_EXCEPTION("이메일 인증번호 일치하지 않습니다."),
  EMAIL_VERIFICATION_NEEDED("이메일 인증이 필요 합니다"),
  AUTHENTICATION_EXCEPTION("로그인하고 이용해주세요."),
  INVALID_TOKEN_EXCEPTION("유효하지 않은 토큰 입니다."),
  AUTHENTICATION_MISMATCH_EXCEPTION("권한이 없습니다."),
  LOGIN_REQUIRED_EXCEPTION("리프레시 토큰 문제 있으니 다시 로그인 해주세요."),

  // guestbook
  NOT_FOUND_GUESTBOOK_EXCEPTION("해당 방명록을 찾을 수 없습니다."),

  // team
  NOT_FOUND_TEAM_EXCEPTION("해당 팀이 존재하지 않습니다."),
  NOT_EQUALS_TEAM_ADMIN_EXCEPTION("팀의 관리자가 아닙니다."),
  NOT_FOUND_TEAM_USER_EXCEPTION("팀에 유저가 속해있지 않습니다."),
  NOT_FOUND_GAME_EXCEPTION("해당 게임은 존재하지 않습니다."),

  // comment
  NOT_FOUND_COMMENT_EXCEPTION("해당 댓글을 찾을 수 없습니다."),

  // chat
  NOT_FOUND_CHATROOM_EXCEPTION("해당 채팅방을 찾을 수 없습니다.");



  private final String message;

  ErrorCode(String message) {
    this.message = message;
  }

}