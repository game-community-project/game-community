package com.gamecommunity.domain.post.service;

import com.gamecommunity.domain.post.dto.PostRequestDto;
import com.gamecommunity.domain.post.entity.Post;
import com.gamecommunity.domain.post.repository.PostRepository;
import com.gamecommunity.domain.user.entity.User;
import com.gamecommunity.global.config.SecurityConfig.AuthenticationHelper;
import com.gamecommunity.global.enums.board.BoardName;
import com.gamecommunity.global.enums.game.name.GameName;
import com.gamecommunity.global.enums.game.type.GameType;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;

  private final PostImageUploadService postImageUploadService;

  private final AuthenticationHelper authenticationHelper;


  @Transactional
  public void createPost(
      PostRequestDto requestDto, GameType gameType, GameName gameName, BoardName boardName,
      MultipartFile file, UserDetailsImpl userDetails) throws IOException {

    User loginUser = authenticationHelper.checkAuthentication(userDetails);

    String imageUrl = null;

    // 파일이 존재하는 경우에만 이미지 업로드를 수행
    if (file != null && !file.isEmpty()) {
      imageUrl = postImageUploadService.uploadFile(file);
    }

    // 게시글 생성
    Post post = new Post(
        requestDto, gameType, gameName, boardName, imageUrl, loginUser);

    postRepository.save(post);
  }
}
