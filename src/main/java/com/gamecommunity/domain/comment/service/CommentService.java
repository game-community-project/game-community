package com.gamecommunity.domain.comment.service;


import com.gamecommunity.domain.comment.dto.CommentRequestDto;
import com.gamecommunity.domain.comment.dto.CommentResponseDto;
import com.gamecommunity.domain.comment.entity.Comment;
import com.gamecommunity.domain.comment.repository.CommentRepository;
import com.gamecommunity.domain.post.entity.Post;
import com.gamecommunity.domain.post.repository.PostRepository;
import com.gamecommunity.domain.post.service.PostService;
import com.gamecommunity.domain.user.entity.User;
import com.gamecommunity.global.exception.common.BusinessException;
import com.gamecommunity.global.exception.common.ErrorCode;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;

  @Transactional
  public void createComment(User user, Long postId,
      CommentRequestDto commentRequestDto) {
    Post post = postRepository.findByPostId(postId).orElseThrow(() ->
        new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_POST_EXCEPTION));
    List<Comment> comments = commentRepository.findAllByPost(post);
    System.out.println("comments = " + comments);
    Long commentRef = null;
    if (!comments.isEmpty()) {
      commentRef = Collections.max(comments, Comparator.comparing(Comment::getRef)).getRef();
    } else {
      commentRef = 0l;
    }

    Long parentId = commentRequestDto.parentId();
    System.out.println("parentId = " + parentId);
    String content = commentRequestDto.content();
    if (parentId == 0) {
      // 최상위 댓글
      Comment comment = Comment.builder()
          .ref(commentRef + 1l)
          .level(0l)
          .refOrder(0l)
          .childCount(0l)
          .parentId(0l)
          .content(content)
          .user(user)
          .post(post)
          .build();
      commentRepository.save(comment);
    } else {
      //대댓글 저장
      // 부모 댓글 데이터
      Comment parentComment = commentRepository.findById(parentId).orElseThrow(() ->
          new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_COMMENT_EXCEPTION));

      Long refOrderResult = refOrderAndUpdate(parentComment);

      Comment comment = Comment.builder()
          .ref(parentComment.getRef())
          .level(parentComment.getLevel() + 1l)
          .refOrder(refOrderResult)
          .childCount(0l)
          .parentId(parentId)
          .content(content)
          .user(user)
          .post(post)
          .build();

      //부모 댓글의 자식컬럼수 + 1 업데이트
      commentRepository.updateChildCount(parentComment.getCommentId(),
          parentComment.getChildCount()+1);

      commentRepository.save(comment);
    }
  }


  private Long refOrderAndUpdate(Comment comment) {

    Long saveLevel = comment.getLevel() + 1l;
    Long refOrder = comment.getRefOrder();
    Long childCount = comment.getChildCount();
    Long ref = comment.getRef();

    //부모 댓글그룹의 자식수
    Long childCountSum = commentRepository.findBySumChildCount(ref);
    //SELECT SUM(childCount) FROM BOARD_COMMENTS WHERE ref = ?1
    //부모 댓글그룹의 최댓값 level
    Long maxLevel = commentRepository.findByNvlMaxLevel(ref);
    //SELECT MAX(level) FROM BOARD_COMMENTS WHERE ref = ?1

    //저장할 대댓글 level과 그룹내의최댓값 level의 조건 비교
        /*
        level + 1 < 그룹리스트에서 max level값  ChildCount sum + 1 * NO UPDATE
        level + 1 = 그룹리스트에서 max level값  refOrder + ChileCount + 1 * UPDATE
        level + 1 > 그룹리스트에서 max level값  refOrder + 1 * UPDATE
        */
    if (saveLevel < maxLevel) {
      return childCountSum + 1l;
    } else if (saveLevel.equals(maxLevel)) {
      commentRepository.updateRefOrderPlus(ref, refOrder + childCount);
      //UPDATE comment SET refOrder = refOrder + 1 WHERE ref = ?1 AND refOrder > ?2
      return refOrder + childCount + 1l;
    } else if (saveLevel > maxLevel) {
      commentRepository.updateRefOrderPlus(ref, refOrder);
      //UPDATE comment SET refOrder = refOrder + 1 WHERE ref = ?1 AND refOrder > ?2
      return refOrder + 1l;
    }

    return null;
  }

  @Transactional
  public CommentResponseDto updateComment(User user, Long commentId,
      CommentRequestDto commentRequestDto) {
    Comment comment = commentRepository.findByCommentId(commentId).orElseThrow(() ->
        new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_COMMENT_EXCEPTION));
    if (!user.getId().equals(comment.getUser().getId())) {
      throw new BusinessException(HttpStatus.BAD_REQUEST,
          ErrorCode.AUTHENTICATION_MISMATCH_EXCEPTION);
    }
    comment.update(commentRequestDto);
    return new CommentResponseDto(comment);

  }


  public void deleteComment(User user, Long commentId) {
    Comment comment = commentRepository.findByCommentId(commentId).orElseThrow(() ->
        new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_COMMENT_EXCEPTION));

    if (!user.getId().equals(comment.getUser().getId())) {
      throw new BusinessException(HttpStatus.BAD_REQUEST,
          ErrorCode.AUTHENTICATION_MISMATCH_EXCEPTION);
    }

    commentRepository.delete(comment);
  }

  public Page<CommentResponseDto> getComments(
      int page, int size, String sortKey, boolean isAsc,
      Long postId) {
    Post post = postRepository.findByPostId(postId).orElseThrow(() ->
        new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_POST_EXCEPTION));

    // 페이징 및 정렬처리
    Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
    Sort sort = Sort.by(direction, sortKey);
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<Comment> commentList = commentRepository
        .findAllByPost(post, pageable);

    return commentList.map(CommentResponseDto::new);
  }

}
