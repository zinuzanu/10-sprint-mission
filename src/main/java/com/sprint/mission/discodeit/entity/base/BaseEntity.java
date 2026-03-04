package com.sprint.mission.discodeit.entity.base;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

@Getter
public abstract class BaseEntity {

  private final UUID id;

  @CreatedDate
  protected Instant createdAt;

  protected BaseEntity() { // 필드 초기화
    this.id = UUID.randomUUID();
  }
}
