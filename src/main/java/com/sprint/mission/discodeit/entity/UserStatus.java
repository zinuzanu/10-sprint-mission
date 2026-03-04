package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.Getter;

@Getter
public class UserStatus extends BaseUpdatableEntity {

  private final User user;
  private Instant lastActiveAt;

  public UserStatus(User user, Instant lastActiveAt) {
    super();
    this.user = user;
    this.lastActiveAt = lastActiveAt;
  }

  public void update(Instant lastOnlineAt) {
    this.lastActiveAt = lastOnlineAt;
    super.update();
  }

  public boolean isOnline() {
    if (getLastActiveAt() == null) {
      return false;
    }
    Instant fiveMinutesAgo = Instant.now().minus(5, ChronoUnit.MINUTES);
    return getLastActiveAt().isAfter(fiveMinutesAgo);
  }
}
