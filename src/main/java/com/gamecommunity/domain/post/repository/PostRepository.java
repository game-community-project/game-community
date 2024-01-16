package com.gamecommunity.domain.post.repository;


import com.gamecommunity.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
