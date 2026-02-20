package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;

public class UserDto {

  public record BinaryContentDto(
      String fileName,
      byte[] data
  ) {

  }

  public record CreateRequest(
      String username,
      String email,
      String password
  ) {

  }

  public record UpdateRequest(
      String newUsername,
      String newEmail,
      String newPassword
  ) {

  }

  public record Response(
      UUID id,
      Instant createdAt,
      Instant updatedAt,
      String username,
      String email,
      UUID profileId,
      boolean online
  ) {

  }
}
