package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "읽음 상태 수정 요청")
public class ReadStatusUpdateRequest {

  @Schema(description = "업데이트할 읽은 시각", example = "2026-02-20T09:00:00Z")
  private Instant lastReadAt;
}