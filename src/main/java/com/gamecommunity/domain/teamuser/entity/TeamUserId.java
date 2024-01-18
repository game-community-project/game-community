package com.gamecommunity.domain.teamuser.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamUserId implements Serializable {


  @Column
  private Long teamId;


  @Column
  private Long userId;
}
