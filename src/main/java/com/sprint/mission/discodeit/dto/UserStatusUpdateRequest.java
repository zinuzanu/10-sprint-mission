package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 상태 수정 요청")
public class UserStatusUpdateRequest {

  @Schema(description = "상태 기록 ID (식별자)", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
  private UUID id;

  @Schema(description = "업데이트할 온라인 시간", example = "2026-02-20T08:00:00Z")
  private Instant newLastActiveAt;
}