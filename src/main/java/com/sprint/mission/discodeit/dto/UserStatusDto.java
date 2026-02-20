package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

public class UserStatusDto {

  public record CreateRequest(
      @Schema(description = "사용자 ID", example = "550e8400-e29b-41d4-a716-446655440000")
      UUID userID,
      @Schema(description = "마지막 온라인 시각", example = "2026-02-20T07:23:52Z")
      Instant lastOnlineAt
  ) {

  }

  public record UpdateRequest(
      @Schema(description = "상태 기록 ID (식별자)", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
      UUID id,
      @Schema(description = "업데이트할 온라인 시간", example = "2026-02-20T08:00:00Z")
      Instant lastOnlineAt
  ) {

  }

  public record Response(
      @Schema(description = "상태 기록 ID", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
      UUID id,
      @Schema(description = "사용자 ID", example = "550e8400-e29b-41d4-a716-446655440000")
      UUID userId,
      @Schema(description = "마지막 온라인 시각", example = "2026-02-20T07:23:52Z")
      Instant lastOnlineAt,
      @Schema(description = "현재 온라인 여부", example = "true")
      boolean isOnline
  ) {

  }
}
