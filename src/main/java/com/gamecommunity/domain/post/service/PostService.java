package com.gamecommunity.domain.post.service;

import static com.gamecommunity.domain.user.entity.UserRoleEnum.ADMIN;

import com.gamecommunity.domain.comment.entity.Comment;
import com.gamecommunity.domain.comment.repository.CommentRepository;
import com.gamecommunity.domain.post.dto.PostRequestDto;
import com.gamecommunity.domain.post.dto.PostResponseDto;
import com.gamecommunity.domain.post.entity.Post;
import com.gamecommunity.domain.post.repository.PostRepository;
import com.gamecommunity.domain.user.entity.User;
import com.gamecommunity.global.config.SecurityConfig.AuthenticationHelper;
import com.gamecommunity.global.exception.common.BusinessException;
import com.gamecommunity.global.exception.common.ErrorCode;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;

  private final PostImageUploadService postImageUploadService;

  private final AuthenticationHelper authenticationHelper;

  private final CommentRepository commentRepository;

  @Transactional
  public PostResponseDto createPost(
      PostRequestDto requestDto, MultipartFile file, UserDetailsImpl userDetails) throws IOException {

    User loginUser = authenticationHelper.checkAuthentication(userDetails);

    String imageUrl = null;

    // 파일이 존재하는 경우에만 이미지 업로드를 수행
    if (file != null && !file.isEmpty()) {
      imageUrl = postImageUploadService.uploadFile(file);
    }

    // 게시글 생성
    Post post = new Post(
        requestDto, imageUrl, loginUser);

    postRepository.save(post);
    return PostResponseDto.fromEntity(post);
  }

  public PostResponseDto getPost(Long postId) {

    Post post = getFindPost(postId);

    return PostResponseDto.fromEntity(post);
  }

  public Page<PostResponseDto> getPosts(
      int page, int size, String sortKey, boolean isAsc) {
    // 페이징 및 정렬처리
    Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
    Sort sort = Sort.by(direction, sortKey);
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<Post> postList = postRepository.findAll(pageable);

    return postList.map(PostResponseDto::fromEntity);
  }

  @Transactional
  public PostResponseDto updatePost(
      Long postId, PostRequestDto requestDto, MultipartFile file, UserDetailsImpl userDetails)
      throws IOException {

    Post post = getAuthenticationPost(postId, userDetails);

    String imageUrl = post.getPostImageUrl();

    // 파일이 존재하는 경우에만 이미지 업로드를 수행
    if (file != null && !file.isEmpty()) {
      imageUrl = postImageUploadService.uploadFile(file);
    }

    post.update(requestDto, imageUrl);
    return PostResponseDto.fromEntity(post);
  }

  @Transactional
  public void deletePost(Long postId, UserDetailsImpl userDetails) {

    Post post = getAuthenticationPost(postId, userDetails);

    postRepository.delete(post);
  }

  @Transactional
  public void closePost(Long postId) {
    Post post = getFindPost(postId);

    post.setClose(true);
    postRepository.save(post);
  }

  @Transactional
  public void acceptComment(Long postId, Long commentId, UserDetailsImpl userDetails) {
    Post post = getAuthenticationPost(postId, userDetails);

    Comment comment = commentRepository.findByCommentId(commentId).orElseThrow(() ->
        new BusinessException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_COMMENT_EXCEPTION));

    comment.setAccepted(true);
    commentRepository.save(comment);
    closePost(comment.getPost().getPostId());
  }

  // 게시글 가져오는 메서드
  public Post getFindPost(Long postId) {
    return postRepository.findById(postId)
        .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
            ErrorCode.NOT_FOUND_POST_EXCEPTION));
  }

  // 인증된 게시글 가져오는 메서드
  public Post getAuthenticationPost(Long postId, UserDetailsImpl userDetails) {
    User loginUser = authenticationHelper.checkAuthentication(userDetails);

    Post post = getFindPost(postId);

    boolean isAdmin = loginUser.getRole().equals(ADMIN);

    // 로그인한 유저가 게시글 작성자나 관리자가 아니면 수정 불가
    if (!isAdmin && !loginUser.getNickname().equals(post.getPostAuthor())) {
      throw new BusinessException(HttpStatus.UNAUTHORIZED,
          ErrorCode.AUTHENTICATION_MISMATCH_EXCEPTION);
    }
    return post;
  }


}
