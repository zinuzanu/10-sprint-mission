package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "읽음 상태 수정 요청")
public class ReadStatusUpdateRequest {

  @Schema(description = "업데이트할 읽은 시각", example = "2026-02-20T09:00:00Z")
  @NotNull(message = "읽은 시각은 필수입니다.")
  private Instant lastReadAt;
}