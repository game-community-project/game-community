package com.gamecommunity.domain.post.repository;


import com.gamecommunity.domain.post.entity.Post;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

  Optional<Post> findByPostId(Long postId);
}
