package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "메시지 생성 요청")
public class MessageCreateRequest {

  @Schema(description = "작성자 ID", example = "550e8400-e29b-41d4-a716-446655440000")
  @NotNull(message = "작성자 ID는 필수입니다.")
  private UUID authorId;

  @Schema(description = "채널 ID", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
  @NotNull(message = "채널 ID는 필수입니다.")
  private UUID channelId;

  @Schema(description = "메시지 내용", example = "오늘 하루도 파이팅 합시다!!")
  @NotBlank(message = "메시지 내용을 입력해 주세요.")
  private String content;
}