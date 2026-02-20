package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {

  private static final long serialVersionUID = 1L;

  private final UUID id;
  private final Instant createdAt;
  private final String fileName;
  private final Long size;
  private final String contentType;
  private final byte[] bytes;

  public BinaryContent(UUID id, Instant createdAt, String fileName, Long size, String contentType,
      byte[] bytes) {
    this.id = id;
    this.createdAt = (createdAt != null) ? createdAt : Instant.now();
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
    this.bytes = bytes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BinaryContent)) {
      return false;
    }
    BinaryContent that = (BinaryContent) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
