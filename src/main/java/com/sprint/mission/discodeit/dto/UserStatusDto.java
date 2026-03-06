package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 상태 정보 응답")
public class UserStatusDto {

  @Schema(description = "상태 기록 ID", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
  private UUID id;

  @Schema(description = "사용자 정보")
  private UserDto user;

  @Schema(description = "마지막 온라인 시각", example = "2026-02-20T07:23:52Z")
  private Instant lastOnlineAt;

  @Schema(description = "현재 온라인 여부", example = "true")
  private boolean isOnline;
}
