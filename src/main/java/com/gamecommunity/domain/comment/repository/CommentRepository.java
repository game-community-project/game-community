package com.gamecommunity.domain.comment.repository;


import com.gamecommunity.domain.comment.entity.Comment;
import com.gamecommunity.domain.post.entity.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  Page<Comment> findAllByPost(Post post, Pageable pageable);

  List<Comment> findAllByPost(Post post);


  Optional<Comment> findByCommentId(Long commentId);

  @Modifying
  @Query("update Comment c Set c.childCount = :childCount where c.commentId = :commentId")
  void updateChildCount(@Param("commentId") Long commentId, @Param("childCount") Long childCount);

  @Query("SELECT SUM(childCount) FROM Comment WHERE ref = :ref")
  Long findBySumChildCount(@Param("ref") Long ref);

  @Query("SELECT MAX(level) FROM Comment WHERE ref = :ref")
  Long findByNvlMaxLevel(@Param("ref") Long ref);


  @Modifying
  @Query("UPDATE Comment c SET c.refOrder = c.refOrder + 1 WHERE c.ref = :ref AND c.refOrder > :refOrder")
  void updateRefOrderPlus(@Param("ref") Long ref, @Param("refOrder") Long refOrder);


}
