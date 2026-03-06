package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "메시지 생성 요청")
public class MessageCreateRequest {

  @Schema(description = "작성자 ID", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID authorId;

  @Schema(description = "채널 ID", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
  private UUID channelId;

  @Schema(description = "메시지 내용", example = "오늘 하루도 파이팅 합시다!!")
  private String content;
}