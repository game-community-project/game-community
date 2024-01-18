package com.gamecommunity.domain.comment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.gamecommunity.domain.comment.dto.CommentRequestDto;
import com.gamecommunity.domain.comment.dto.CommentResponseDto;
import com.gamecommunity.domain.comment.entity.Comment;
import com.gamecommunity.domain.comment.repository.CommentRepository;
import com.gamecommunity.domain.post.entity.Post;
import com.gamecommunity.domain.post.repository.PostRepository;
import com.gamecommunity.domain.test.CommentTest;
import com.gamecommunity.domain.user.entity.User;
import com.gamecommunity.global.exception.common.BusinessException;
import com.gamecommunity.global.exception.common.ErrorCode;
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

@DisplayName("댓글 서비스 테스트")
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CommentServiceTest implements CommentTest {

  @InjectMocks
  private CommentService commentService;
  @Mock
  private CommentRepository commentRepository;
  @Mock
  private PostRepository postRepository;

  @Test
  @DisplayName("댓글 작성 - 성공")
  void createCommentTestSuccess() {

    // given
    CommentRequestDto commentRequestDto1 = new CommentRequestDto(TEST_COMMENT_CONTENT);
    CommentRequestDto commentRequestDto2 = new CommentRequestDto(TEST_COMMENT_CONTENT);
    Post post = TEST_POST;
    Long postId = TEST_POST_ID;
    User loginUser = TEST_USER;

    given(postRepository.findByPostId(postId)).willReturn(Optional.of(post));

    // when
    commentService.createComment(loginUser, post.getPostId(), commentRequestDto1);

    // Then
    verify(commentRepository, times(1)).save(any(Comment.class));
  }

  @Test
  @DisplayName("게시글 페이징 조회 - 성공")
  void getPostsTestSuccess() {

    // given
    CommentRequestDto commentRequestDto1 = new CommentRequestDto(TEST_COMMENT_CONTENT);
    CommentRequestDto commentRequestDto2 = new CommentRequestDto(TEST_COMMENT_CONTENT);
    Post post = TEST_POST;
    Long postId = TEST_POST_ID;
    User loginUser = TEST_USER;

    // given
    int page = 1;
    int size = 10;
    String sortKey = "createdAt";
    boolean isAsc = true;

    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortKey));

    List<Comment> comments = Arrays.asList(TEST_COMMENT, TEST_ANOTHER_COMMENT);

    Page<Comment> commentPage = new PageImpl<>(comments, pageable, comments.size());

    given(postRepository.findByPostId(postId)).willReturn(Optional.of(post));
    given(commentRepository.findAllByPost(post, pageable)).willReturn(commentPage);

    // when
    Page<CommentResponseDto> result = commentService.getComments(page, size, sortKey, isAsc,
        postId);

    // then
    assertEquals(commentPage.getTotalElements(), result.getTotalElements());
    assertEquals(commentPage.getContent().size(), result.getContent().size());
  }

  @Test
  @DisplayName("댓글 수정 - 성공")
  void updateCommentTestSuccess() {

    // given
    User loginUser = TEST_USER;
    CommentRequestDto commentRequestDto = new CommentRequestDto(TEST_ANOTHER_COMMENT_CONTENT);
    Long commentId = TEST_COMMENT_ID;
    Comment comment = TEST_COMMENT;

    given(commentRepository.findByCommentId(commentId)).willReturn(Optional.of(comment));

    // when
    commentService.updateComment(loginUser, commentId, commentRequestDto);

    // then
    assertEquals(TEST_ANOTHER_COMMENT_CONTENT, comment.getContent());
  }

  @Test
  @DisplayName("댓글 수정 - 실패(댓글 주인이 아님)")
  void updateCommentTestFail() {

    // given
    User loginUser = TEST_USER;
    User nonOwnerUser = TEST_ANOTHER_USER;
    CommentRequestDto commentRequestDto = new CommentRequestDto(TEST_ANOTHER_COMMENT_CONTENT);
    Long commentId = TEST_COMMENT_ID;
    Comment comment = TEST_COMMENT;

    given(commentRepository.findByCommentId(commentId)).willReturn(Optional.of(comment));

    // when
    BusinessException ex = assertThrows(BusinessException.class, () -> {
      commentService.updateComment(nonOwnerUser, commentId, commentRequestDto);
    });

    // then
    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    assertEquals(ErrorCode.AUTHENTICATION_MISMATCH_EXCEPTION.getMessage(), ex.getMessage());
  }

  @Test
  @DisplayName("댓글 삭제 - 성공")
  void deleteCommentTestSuccess() {

    // given
    User loginUser = TEST_USER;
    Long commentId = TEST_COMMENT_ID;
    Comment comment = TEST_COMMENT;

    given(commentRepository.findByCommentId(commentId)).willReturn(Optional.of(comment));

    // when
    commentService.deleteComment(loginUser, commentId);

    // then
    verify(commentRepository, times(1)).delete(comment);
  }

  @Test
  @DisplayName("댓글 삭제 - 성공(댓글의 주인이 아님")
  void deleteCommentTestFail() {

    // given
    User loginUser = TEST_USER;
    User nonOwnerUser = TEST_ANOTHER_USER;
    Long commentId = TEST_COMMENT_ID;
    Comment comment = TEST_COMMENT;

    given(commentRepository.findByCommentId(commentId)).willReturn(Optional.of(comment));

    // when
    BusinessException ex = assertThrows(BusinessException.class, () -> {
      commentService.deleteComment(nonOwnerUser, commentId);
    });

    // then
    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    assertEquals(ErrorCode.AUTHENTICATION_MISMATCH_EXCEPTION.getMessage(), ex.getMessage());
  }

}
