package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "비공개 채널 생성 요청")
public class ChannelCreatePrivateRequest {

  @Schema(description = "참여할 사용자 ID 리스트", example = "[\"550e8400-e29b-41d4-a716-446655440000\"]")
  private List<UUID> participantIds;
}