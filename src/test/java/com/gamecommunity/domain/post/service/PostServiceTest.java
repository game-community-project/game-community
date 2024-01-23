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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    postService.createPost(requestDto, file, TEST_USER_DETAILS);

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

  @Test
  @DisplayName("게시글 페이징 조회 - 성공")
  void getPostsTestSuccess() {

    // given
    int page = 1;
    int size = 10;
    String sortKey = "createdAt";
    boolean isAsc = true;

    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortKey));

    List<Post> posts = Arrays.asList(TEST_POST, TEST_ANOTHER_POST);

    Page<Post> postPage = new PageImpl<>(posts, pageable, posts.size());

    given(postRepository.findAll(pageable))
        .willReturn(postPage);

    // when
    Page<PostResponseDto> result = postService.getPosts(
        page, size, sortKey, isAsc);

    // then
    assertEquals(postPage.getTotalElements(), result.getTotalElements());
    assertEquals(postPage.getContent().size(), result.getContent().size());
  }

  @Test
  @DisplayName("게시글 수정 - 성공")
  void updatePostTestSuccess() throws IOException {

    // given
    Long postId = TEST_POST_ID;
    Post post = TEST_POST;
    MultipartFile file = mock(MultipartFile.class);
    UserDetailsImpl userDetails = TEST_USER_DETAILS;
    User loginUser = userDetails.getUser();

    given(authenticationHelper.checkAuthentication(userDetails)).willReturn(loginUser);

    PostRequestDto requestDto = new PostRequestDto(
        TEST_ANOTHER_POST.getPostTitle(), TEST_ANOTHER_POST.getPostContent());

    given(postRepository.findById(postId)).willReturn(Optional.of(post));

    // when
    postService.updatePost(postId, requestDto, file, userDetails);

    // then
    assertEquals(TEST_ANOTHER_POST.getPostTitle(), post.getPostTitle());
    assertEquals(TEST_ANOTHER_POST.getPostContent(), post.getPostContent());
  }

  @Test
  @DisplayName("게시글 수정 - 실패(로그한 유저가 게시글 작성자가 아님")
  void updatePostTestFailureNotAuth() throws IOException {

    // given
    Long postId = TEST_ANOTHER_POST_ID;
    Post post = TEST_ANOTHER_POST;
    MultipartFile file = mock(MultipartFile.class);

    UserDetailsImpl userDetails = TEST_USER_DETAILS;
    User loginUser = userDetails.getUser();

    given(authenticationHelper.checkAuthentication(userDetails)).willReturn(loginUser);

    PostRequestDto requestDto = new PostRequestDto(
        TEST_ANOTHER_POST.getPostTitle(), TEST_ANOTHER_POST.getPostContent());

    given(postRepository.findById(postId)).willReturn(Optional.of(post));

    // when, then
    BusinessException ex = assertThrows(BusinessException.class, () -> {
      postService.updatePost(postId, requestDto, file, userDetails);
    });

    assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatus());
    assertEquals(ErrorCode.AUTHENTICATION_MISMATCH_EXCEPTION.getMessage(), ex.getMessage());
  }

  @Test
  @DisplayName("게시글 삭제 - 성공")
  void deletePostTestSuccess() {

    // given
    Long postId = TEST_POST_ID;
    Post post = TEST_POST;
    UserDetailsImpl userDetails = TEST_USER_DETAILS;
    User loginUser = userDetails.getUser();

    given(authenticationHelper.checkAuthentication(userDetails)).willReturn(loginUser);

    given(postRepository.findById(postId)).willReturn(Optional.of(post));

    // when
    postService.deletePost(postId, userDetails);

    // then
    verify(postRepository, times(1)).delete(post);
  }

  @Test
  @DisplayName("게시글 삭제 - 실패(로그한 유저가 게시글 작성자가 아님")
  void deletePostTestFailureNotAuth() {

    // given
    Long postId = TEST_ANOTHER_POST_ID;
    Post post = TEST_ANOTHER_POST;
    UserDetailsImpl userDetails = TEST_USER_DETAILS;
    User loginUser = userDetails.getUser();

    given(authenticationHelper.checkAuthentication(userDetails)).willReturn(loginUser);

    given(postRepository.findById(postId)).willReturn(Optional.of(post));

    // when, then
    BusinessException ex = assertThrows(BusinessException.class, () -> {
      postService.deletePost(postId, userDetails);
    });

    assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatus());
    assertEquals(ErrorCode.AUTHENTICATION_MISMATCH_EXCEPTION.getMessage(), ex.getMessage());
  }

}
