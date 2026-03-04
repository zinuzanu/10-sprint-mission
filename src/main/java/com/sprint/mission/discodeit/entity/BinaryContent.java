package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import lombok.Getter;

@Getter
public class BinaryContent extends BaseEntity {

  private final String fileName;
  private final Long size;
  private final String contentType;
  private final byte[] bytes;

  public BinaryContent(String fileName, Long size, String contentType, byte[] bytes) {
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
    this.bytes = bytes;
  }
}
