package com.gamecommunity.domain.test;

import com.gamecommunity.domain.post.dto.PostRequestDto;
import com.gamecommunity.domain.post.dto.PostResponseDto;
import com.gamecommunity.domain.post.entity.Post;
import com.gamecommunity.domain.post.entity.PostLike;
import com.gamecommunity.domain.user.entity.User;

public interface PostTest extends UserTest {

  String ANOTHER_PREFIX = "another-";
  Long TEST_POST_ID = 1L;
  Long TEST_ANOTHER_POST_ID = 2L;
  String TEST_POST_TITLE = "title";
  String TEST_POST_CONTENT = "content";
  String TEST_POST_IMAGE_URL = "url";
  Integer TEST_REPORT = 0;
  Integer TEST_POST_LIKE = 0;
  Integer TEST_POST_Unlike = 0;
  User TEST_USER = UserTest.TEST_USER;

  Post TEST_POST = Post.builder()
      .postId(TEST_POST_ID)
      .postTitle(TEST_POST_TITLE)
      .postContent(TEST_POST_CONTENT)
      .postImageUrl(TEST_POST_IMAGE_URL)
      .postAuthor(TEST_USER.getNickname())
      .report(TEST_REPORT)
      .postLike(TEST_POST_LIKE)
      .postUnlike(TEST_POST_Unlike)
      .close(false)
      .user(TEST_USER)
      .build();

  Post TEST_ANOTHER_POST = Post.builder()
      .postId(TEST_ANOTHER_POST_ID)
      .postTitle(ANOTHER_PREFIX + TEST_POST_TITLE)
      .postContent(ANOTHER_PREFIX + TEST_POST_CONTENT)
      .postImageUrl(ANOTHER_PREFIX + TEST_POST_IMAGE_URL)
      .postAuthor(ANOTHER_PREFIX + TEST_USER.getNickname())
      .report(TEST_REPORT)
      .postLike(TEST_POST_LIKE)
      .postUnlike(TEST_POST_Unlike)
      .close(false)
      .user(TEST_ANOTHER_USER)
      .build();

  PostRequestDto TEST_REQUEST_DTO = PostRequestDto.builder()
      .postTitle(TEST_POST_TITLE)
      .postContent(TEST_POST_CONTENT)
      .build();

  PostResponseDto TEST_RESPONSE_DTO = PostResponseDto.builder()
      .postId(TEST_POST_ID)
      .postTitle(TEST_POST_TITLE)
      .postContent(TEST_POST_CONTENT)
      .postImageUrl(TEST_POST_IMAGE_URL)
      .postAuthor(TEST_USER.getNickname())
      .report(TEST_REPORT)
      .postLike(TEST_POST_LIKE)
      .postUnlike(TEST_POST_Unlike)
      .close(false)
      .build();

  PostLike TEST_POSTLIKE = PostLike.builder()
      .postLikeId(TEST_POST_ID)
      .islike(true)
      .post(TEST_POST)
      .user(TEST_ANOTHER_USER)
      .build();
}
