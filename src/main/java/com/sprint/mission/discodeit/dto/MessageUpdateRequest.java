package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "메시지 수정 요청")
public class MessageUpdateRequest {

  @Schema(description = "수정할 메시지 내용", example = "오늘 날씨가 춥네요!!")
  @NotBlank(message = "메시지 내용을 입력해 주세요.")
  private String newContent;
}