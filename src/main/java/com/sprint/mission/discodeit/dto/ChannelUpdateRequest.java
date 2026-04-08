package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "채널 정보 수정 요청")
public class ChannelUpdateRequest {

  @Schema(description = "변경할 채널 이름", example = "스프링-스터디-방")
  @NotBlank(message = "채널 이름은 필수입니다.")
  @Size(max = 100, message = "채널 이름은 100자 이내로 입력해야 합니다.")
  private String newName;

  @Schema(description = "변경할 채널 설명", example = "스프링 프레임워크 학습용으로 변경합니다.")
  @Size(max = 500, message = "채널 설명은 500자 이내로 입력해야 합니다.")
  private String newDescription;
}