package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "읽음 상태 생성 요청")
public class ReadStatusCreateRequest {

  @Schema(description = "사용자 ID", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID userId;

  @Schema(description = "채널 ID", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
  private UUID channelId;

  @Schema(description = "마지막으로 읽은 시각", example = "2026-02-20T08:40:00Z")
  private Instant lastReadAt;
}