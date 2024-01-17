package com.gamecommunity.domain.user.entity;


import com.gamecommunity.domain.guestbook.entity.Guestbook;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "users")
public class User {

  private Long kakaoId;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, unique = true)
  private String nickname;

  @Column(nullable = false)
  private String password;

  @Column()
  private String introduction;

  @Column()
  private Instant blockDate;

  @Column(nullable = false)
  private int ranking;

  @Column()
  private String profileUrl;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private UserRoleEnum role;

  @OneToMany(mappedBy = "toUser", cascade = CascadeType.REMOVE)
  private List<Guestbook> guestbookList;

  public void updatePassword(String password) {
    this.password = password;
  }

  public void addKakaoId(Long kakaoId) {
    this.kakaoId = kakaoId;
  }

  public void modifyProfile(String introduction, String profileUrl) {
    this.introduction = introduction;
    this.profileUrl = profileUrl;
  }


}
