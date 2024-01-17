package com.gamecommunity.domain.post.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.gamecommunity.domain.post.entity.Post;
import com.gamecommunity.domain.post.entity.PostLike;
import com.gamecommunity.domain.post.repository.PostLikeRepository;
import com.gamecommunity.domain.post.repository.PostRepository;
import com.gamecommunity.domain.test.PostTest;
import com.gamecommunity.domain.user.entity.User;
import com.gamecommunity.global.config.SecurityConfig.AuthenticationHelper;
import com.gamecommunity.global.exception.common.BusinessException;
import com.gamecommunity.global.exception.common.ErrorCode;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("게시글 좋아요 서비스 테스트")
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PostLikeServiceTest implements PostTest {

  @InjectMocks
  private PostLikeService postLikeService;
  @Mock
  private PostLikeRepository postLikeRepository;
  @Mock
  private PostRepository postRepository;
  @Mock
  private PostService postService;
  @Mock
  private AuthenticationHelper authenticationHelper;

  // islike가 true : 좋아요, false : 싫어요

  @Test
  @DisplayName("게시글 좋아요 또는 싫어요 - 성공")
  void addLikeTestSuccess() {

    // given
    Long postId = TEST_ANOTHER_POST_ID;
    Post post = TEST_ANOTHER_POST;
    UserDetailsImpl userDetails = TEST_USER_DETAILS;
    User loginUser = userDetails.getUser();
    Boolean islike = true;

    given(authenticationHelper.checkAuthentication(userDetails)).willReturn(loginUser);

    given(postService.getFindPost(postId)).willReturn(post);

    given(postLikeRepository.existsByUserAndPost(loginUser, post)).willReturn(false);

    // when
    postLikeService.addLike(postId, islike, userDetails);

    // then
    verify(postLikeRepository, times(1)).save(any(PostLike.class));
    verify(postRepository, times(1)).save(post);
    assertEquals(TEST_POST_LIKE + 1, post.getPostLike());
  }

  @Test
  @DisplayName("게시글 좋아요 또는 싫어요 - 실패(자신의 글)")
  void addLikeTestFailureSelfLike() {

    // given
    Long postId = TEST_POST_ID;
    Post post = TEST_POST;
    UserDetailsImpl userDetails = TEST_USER_DETAILS;
    User loginUser = userDetails.getUser();
    Boolean islike = true;

    given(authenticationHelper.checkAuthentication(userDetails)).willReturn(loginUser);

    given(postService.getFindPost(postId)).willReturn(post);

    // when, then
    BusinessException ex = assertThrows(BusinessException.class, () -> {
      postLikeService.addLike(postId, islike, userDetails);
    });

    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    assertEquals(ErrorCode.SELF_LIKE_EXCEPTION.getMessage(), ex.getMessage());
  }

  @Test
  @DisplayName("게시글 좋아요 또는 싫어요 - 실패(좋아요 또는 싫어요 내역이 존재)")
  void addLikeTestFailureDuplicatedLike() {

    // given
    Long postId = TEST_POST_ID;
    Post post = TEST_POST;
    UserDetailsImpl userDetails = TEST_ANOTHER_USER_DETAILS;
    User loginUser = userDetails.getUser();
    Boolean islike = true;

    given(authenticationHelper.checkAuthentication(userDetails)).willReturn(loginUser);

    given(postService.getFindPost(postId)).willReturn(post);
    // 내역이 이미 존재하도록 설정
    given(postLikeRepository.existsByUserAndPost(loginUser, post)).willReturn(true);

    // when, then
    BusinessException ex = assertThrows(BusinessException.class, () -> {
      postLikeService.addLike(postId, islike, userDetails);
    });

    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    assertEquals(ErrorCode.DUPLICATED_LIKE_EXCEPTION.getMessage(), ex.getMessage());
  }
}
