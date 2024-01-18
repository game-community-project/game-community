package com.gamecommunity.domain.test;


import com.gamecommunity.domain.comment.entity.Comment;
import com.gamecommunity.domain.post.entity.Post;
import com.gamecommunity.domain.user.entity.User;

public interface CommentTest extends PostTest, UserTest {

  Long TEST_COMMENT_ID = 1L;
  Long TEST_ANOTHER_COMMENT_ID = 2L;
  String TEST_COMMENT_CONTENT = "content1";
  String TEST_ANOTHER_COMMENT_CONTENT = "content2";
  User TEST_USER = UserTest.TEST_USER;
  Post TEST_POST = PostTest.TEST_POST;

  Comment TEST_COMMENT = Comment.builder()
      .commentId(TEST_COMMENT_ID)
      .content(TEST_COMMENT_CONTENT)
      .user(TEST_USER)
      .post(TEST_POST)
      .build();

  Comment TEST_ANOTHER_COMMENT = Comment.builder()
      .commentId(TEST_ANOTHER_COMMENT_ID)
      .content(TEST_ANOTHER_COMMENT_CONTENT)
      .user(TEST_USER)
      .post(TEST_POST)
      .build();

}
