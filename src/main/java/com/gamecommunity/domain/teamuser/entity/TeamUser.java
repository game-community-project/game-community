package com.gamecommunity.domain.teamuser.entity;

import com.gamecommunity.domain.team.entity.Team;
import com.gamecommunity.domain.user.entity.User;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "team_users")
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class TeamUser {

  @EmbeddedId
  TeamUserId id;


  @ManyToOne
  @MapsId("teamId")
  @JoinColumn(name = "team_id")
  private Team team;

  @ManyToOne
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  private User user;

  public TeamUser(Team team, User user){
    this.id= new TeamUserId(team.getTeamId(), user.getId());
    this.team = team;
    this.user = user;

  }


}
