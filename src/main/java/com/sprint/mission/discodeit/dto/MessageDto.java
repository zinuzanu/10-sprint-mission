package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class MessageDto {

  public record CreateRequest(
      UUID authorId,
      UUID channelId,
      String content
  ) {

  }

  public record UpdateRequest(
      String newContent
  ) {

  }

  public record Response(
      UUID id,
      String content,
      UUID authorId,
      UUID channelId,
      Instant createdAt,
      Instant updatedAt,
      List<UUID> attachmentIds
  ) {

  }
}
