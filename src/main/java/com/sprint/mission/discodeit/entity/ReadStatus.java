package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import java.time.Instant;
import lombok.Getter;

@Getter
public class ReadStatus extends BaseUpdatableEntity {

  private final User user;
  private final Channel channel;
  private Instant lastReadAt;

  public ReadStatus(User user, Channel channel, Instant lastReadAt) {
    this.user = user;
    this.channel = channel;
    this.lastReadAt = lastReadAt;
  }

  public void update(Instant lastReadAt) {
    this.lastReadAt = Instant.now();
    super.update();
  }
}
