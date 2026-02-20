package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;

public class BinaryContentDto {

  public record CreateRequest(
      String fileName,
      byte[] bytes,
      Long size,
      String contentType,
      Instant createdAt
  ) {

  }

  public record Response(
      UUID id,
      Instant createdAt,
      String fileName,
      Long size,
      String contentType,
      byte[] bytes
  ) {

  }
}
