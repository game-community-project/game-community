package com.gamecommunity.domain.test;

import com.gamecommunity.domain.user.entity.User;
import com.gamecommunity.domain.user.entity.UserRoleEnum;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import java.time.Instant;

public interface UserTest {

  String ANOTHER_PREFIX = "another-";
  String ADMIN_PREFIX = "admin-";
  Long TEST_USER_ID = 1L;
  Long TEST_ANOTHER_USER_ID = 2L;
  Long TEST_ADMIN_USER_ID = 3L;
  String TEST_EMAIL = "user@email.com";
  String TEST_PASSWORD = "password";
  String TEST_NICKNAME = "nickname";
  String TEST_INTRODUCTION = "intro";
  Instant TEST_BLOCK_DATE = Instant.now();
  int TEST_RANKING = 0;
  String TEST_PROFILE_URL = "url";

  User TEST_USER = User.builder()
      .id(TEST_USER_ID)
      .email(TEST_EMAIL)
      .password(TEST_PASSWORD)
      .nickname(TEST_NICKNAME)
      .introduction(TEST_INTRODUCTION)
      .blockDate(TEST_BLOCK_DATE)
      .ranking(TEST_RANKING)
      .profileUrl(TEST_PROFILE_URL)
      .role(UserRoleEnum.USER)
      .build();

  User TEST_ANOTHER_USER = User.builder()
      .id(TEST_ANOTHER_USER_ID)
      .email(ANOTHER_PREFIX + TEST_EMAIL)
      .password(ANOTHER_PREFIX + TEST_PASSWORD)
      .nickname(ANOTHER_PREFIX + TEST_NICKNAME)
      .introduction(ANOTHER_PREFIX + TEST_INTRODUCTION)
      .blockDate(TEST_BLOCK_DATE)
      .ranking(TEST_RANKING)
      .profileUrl(ANOTHER_PREFIX + TEST_PROFILE_URL)
      .role(UserRoleEnum.USER)
      .build();

  User TEST_ADMIN_USER = User.builder()
      .id(TEST_ADMIN_USER_ID)
      .email(ADMIN_PREFIX + TEST_EMAIL)
      .password(ADMIN_PREFIX + TEST_PASSWORD)
      .nickname(ADMIN_PREFIX + TEST_NICKNAME)
      .introduction(ADMIN_PREFIX + TEST_INTRODUCTION)
      .ranking(TEST_RANKING)
      .profileUrl(ADMIN_PREFIX + TEST_PROFILE_URL)
      .role(UserRoleEnum.ADMIN)
      .build();

  UserDetailsImpl TEST_USER_DETAILS = new UserDetailsImpl(TEST_USER);
  UserDetailsImpl TEST_ANOTHER_USER_DETAILS = new UserDetailsImpl(TEST_ANOTHER_USER);
}
