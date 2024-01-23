package com.gamecommunity.domain.post.entity;

import com.gamecommunity.domain.comment.entity.Comment;
import com.gamecommunity.domain.post.dto.PostRequestDto;
import com.gamecommunity.domain.user.entity.User;
import com.gamecommunity.global.auditing.TimeStamped;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "posts")
public class Post extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long postId;

  @Column(nullable = false)
  private String postTitle;

  @Column(nullable = false)
  private String postContent;

  @Column
  private String postImageUrl;

  @Column(nullable = false)
  private String postAuthor;

  @Column(nullable = false)
  private Integer report;

  @Column(nullable = false)
  private Integer postLike;

  @Column(nullable = false)
  private Integer postUnlike;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
  private List<PostLike> postLikes;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
  private List<Comment> comments;

  public Post(PostRequestDto requestDto, String postImageUrl, User loginUser) {
    this.postId = getPostId();
    this.postTitle = requestDto.postTitle();
    this.postContent = requestDto.postContent();
    this.postImageUrl = postImageUrl;
    this.postAuthor = loginUser.getNickname();
    this.report = 0;
    this.postLike = 0;
    this.postUnlike = 0;
    this.user = loginUser;
  }

  public void update(PostRequestDto requestDto, String postImageUrl) {
    this.postTitle = requestDto.postTitle();
    this.postContent = requestDto.postContent();
    this.postImageUrl = postImageUrl;
  }

  public void setPostLike(int postLike) {
    this.postLike = postLike;
  }

  public void setPostUnlike(int postUnlike) {
    this.postUnlike = postUnlike;
  }

}
