package com.gamecommunity.domain.post.repository;

import com.gamecommunity.domain.post.entity.Post;
import com.gamecommunity.domain.post.entity.PostLike;
import com.gamecommunity.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

  boolean existsByUserAndPost(User loginUser, Post post);

  PostLike findByUserAndIslikeAndPost(User loginUser, Boolean isLike, Post post);

}
