package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 상태 생성 요청")
public class UserStatusCreateRequest {

  @Schema(description = "사용자 ID", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID userId;

  @Schema(description = "마지막 온라인 시각", example = "2026-02-20T07:23:52Z")
  private Instant lastOnlineAt;
}