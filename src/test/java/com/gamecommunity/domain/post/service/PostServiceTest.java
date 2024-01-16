package com.gamecommunity.domain.post.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.gamecommunity.domain.post.dto.PostRequestDto;
import com.gamecommunity.domain.post.dto.PostResponseDto;
import com.gamecommunity.domain.post.entity.Post;
import com.gamecommunity.domain.post.repository.PostRepository;
import com.gamecommunity.domain.test.PostTest;
import com.gamecommunity.domain.user.entity.User;
import com.gamecommunity.global.config.SecurityConfig.AuthenticationHelper;
import com.gamecommunity.global.exception.common.BusinessException;
import com.gamecommunity.global.exception.common.ErrorCode;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

  @DisplayName("게시글 서비스 테스트")
  @ActiveProfiles("test")
  @ExtendWith(MockitoExtension.class)
  class PostServiceTest implements PostTest {

    @InjectMocks
    private PostService postService;
    @Mock
    private PostRepository postRepository;
    @Mock
    private PostImageUploadService postImageUploadService;
    @Mock
    private AuthenticationHelper authenticationHelper;

    @Test
    @DisplayName("게시글 작성 - 성공")
    void createPostTestSuccess() throws IOException {

      // given
      PostRequestDto requestDto = TEST_REQUEST_DTO;
      MultipartFile file = mock(MultipartFile.class);
      UserDetailsImpl userDetails = TEST_USER_DETAILS;
      User loginUser = userDetails.getUser();

      given(authenticationHelper.checkAuthentication(userDetails)).willReturn(loginUser);

      given(postImageUploadService.uploadFile(file)).willReturn(TEST_POST_IMAGE_URL);

      // when
      postService.createPost(requestDto, TEST_GAME_TYPE, TEST_GAME_NAME, TEST_BOARD_NAME,
          file, TEST_USER_DETAILS);

      // Then
      verify(postImageUploadService, times(1)).uploadFile(file);
      verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("게시글 조회 - 성공")
    void getPostTestSuccess() {

      // given
      Long postId = TEST_POST_ID;
      Post post = TEST_POST;
      given(postRepository.findById(postId)).willReturn(Optional.of(post));

      // when
      PostResponseDto result = postService.getPost(postId);

      // then
      assertEquals(post.getPostTitle(), result.postTitle());
      assertEquals(post.getPostContent(), result.postContent());
    }

    @Test
    @DisplayName("게시글 조회 - 실패(게시글을 찾을 수 없음)")
    void getPostTestFailureNotFoundPost() {

      // given
      Long postId = TEST_ANOTHER_POST_ID;
      given(postRepository.findById(postId)).willReturn(Optional.empty());

      // when, then
      // 예외가 발생하는지 확인하는 로직
      BusinessException ex = assertThrows(BusinessException.class, () -> {
        postService.getPost(postId);
      });

      assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
      assertEquals(ErrorCode.NOT_FOUND_POST_EXCEPTION.getMessage(), ex.getMessage());

      verify(postRepository, times(1)).findById(postId);
    }

  }
