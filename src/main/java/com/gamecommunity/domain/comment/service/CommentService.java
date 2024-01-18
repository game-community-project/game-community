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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final PostService postService;

  public CommentResponseDto createComment(User user, Long postId,
      CommentRequestDto commentRequestDto) {
    Post post = postRepository.findByPostId(postId).orElseThrow(() ->
        new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_POST_EXCEPTION));
    Comment comment = new Comment(user, post, commentRequestDto);
    commentRepository.save(comment);
    return new CommentResponseDto(comment);
  }
}

