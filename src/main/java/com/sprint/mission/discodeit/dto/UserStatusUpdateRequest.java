package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 상태 수정 요청")
public class UserStatusUpdateRequest {

  @Schema(description = "상태 기록 ID (식별자)", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
  @NotNull(message = "상태 기록 ID는 필수입니다.")
  private UUID id;

  @Schema(description = "업데이트할 온라인 시각", example = "2026-02-20T08:00:00Z")
  @NotNull(message = "업데이트 할 온라인 시각은 필수입니다.")
  private Instant newLastActiveAt;
}