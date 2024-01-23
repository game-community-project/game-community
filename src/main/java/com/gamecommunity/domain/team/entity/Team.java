package com.gamecommunity.domain.team.entity;

import com.gamecommunity.domain.team.dto.TeamRequestDto;
import com.gamecommunity.domain.teamuser.entity.TeamUser;
import com.gamecommunity.global.auditing.TimeStamped;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "teams")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long teamId;

  @Column(nullable = false)
  private Long teamAdminId;

  @Column(nullable = false, unique = true)
  private String teamName;

  @Column(nullable = false)
  private String teamIntroduction;

  @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<TeamUser> teamUsers = new ArrayList<>();

  public Team(Long adminId, TeamRequestDto teamRequestDto) {
    this.teamAdminId = adminId;
    this.teamName = teamRequestDto.teamName();
    this.teamIntroduction = teamRequestDto.teamIntroduction();
    this.teamUsers = new ArrayList<>();
  }

  public void update(TeamRequestDto teamRequestDto) {
    this.teamName = teamRequestDto.teamName();
    this.teamIntroduction = teamRequestDto.teamIntroduction();
  }
}
