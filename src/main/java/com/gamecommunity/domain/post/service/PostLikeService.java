package com.gamecommunity.domain.post.service;

import com.gamecommunity.domain.post.entity.Post;
import com.gamecommunity.domain.post.entity.PostLike;
import com.gamecommunity.domain.post.repository.PostLikeRepository;
import com.gamecommunity.domain.post.repository.PostRepository;
import com.gamecommunity.domain.user.entity.User;
import com.gamecommunity.global.config.SecurityConfig.AuthenticationHelper;
import com.gamecommunity.global.exception.common.BusinessException;
import com.gamecommunity.global.exception.common.ErrorCode;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

  private final PostLikeRepository postLikeRepository;

  private final PostService postService;

  private final PostRepository postRepository;

  private final AuthenticationHelper authenticationHelper;

  public void addLike(Long postId, Boolean isLike, UserDetailsImpl userDetails) {

    User loginUser = authenticationHelper.checkAuthentication(userDetails);

    Post post = postService.getFindPost(postId);

    // 현재 로그인한 유저와 게시글의 생성자가 같다면 예외발생
    if (loginUser.getId().equals(post.getUser().getId())) {
      throw new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.SELF_LIKE_EXCEPTION);
    }

    // 좋아요 또는 싫어요 내역이 있으면 예외발생
    if (postLikeRepository.existsByUserAndPost(loginUser, post)) {
      throw new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.DUPLICATED_LIKE_EXCEPTION);
    }

    PostLike postLike = PostLike.fromUserAndPost(loginUser, isLike, post);

    postLikeRepository.save(postLike);

    // 좋아요 또는 싫어요 수 증가
    updatePostLikePlus(post, isLike);

    postRepository.save(post);
  }

  @Transactional
  public void cancelLike(Long postId, Boolean isLike, User loginUser) {

    Post post = postService.getFindPost(postId);

    // 좋아요 또는 싫어요 내역이 없으면 예외발생
    PostLike postLike = postLikeRepository.findByUserAndIslikeAndPost(loginUser, isLike, post);

    postLikeRepository.delete(postLike);

    // 좋아요 또는 싫어요 수 감소
    updatePostLikeMinus(post, isLike);

    postRepository.save(post);
  }

  private void updatePostLikePlus(Post post, Boolean isLike) {
    if (isLike) {
      post.setPostLike(post.getPostLike() + 1);
    } else {
      post.setPostUnlike(post.getPostUnlike() + 1);
    }
  }

  private void updatePostLikeMinus(Post post, Boolean isLike) {
    if (isLike) {
      post.setPostLike(post.getPostLike() - 1);
    } else {
      post.setPostUnlike(post.getPostUnlike() - 1);
    }
  }

}
