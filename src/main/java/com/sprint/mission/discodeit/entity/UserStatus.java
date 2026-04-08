package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_statuses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStatus extends BaseUpdatableEntity {

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "user_id",
      nullable = false,
      unique = true
  )
  private User user;

  @Column(name = "last_active_at", nullable = false)
  private Instant lastActiveAt;

  public UserStatus(User user) {
    super();
    this.user = user;
    this.lastActiveAt = Instant.now();
  }

  public void updateActiveTime() {
    this.lastActiveAt = Instant.now();
  }

  public boolean isOnline() {
    if (getLastActiveAt() == null) {
      return false;
    }
    Instant fiveMinutesAgo = Instant.now().minus(5, ChronoUnit.MINUTES);
    return getLastActiveAt().isAfter(fiveMinutesAgo);
  }

  @Override
  public String toString() {
    return "UserStatus{" +
        "user=" + user +
        ", lastActiveAt=" + lastActiveAt +
        '}';
  }
}
