package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "채널 정보 수정 요청")
public class ChannelUpdateRequest {

  @Schema(description = "변경할 채널 이름", example = "스프링-스터디-방")
  private String newName;

  @Schema(description = "변경할 채널 설명", example = "스프링 프레임워크 학습용으로 변경합니다.")
  private String newDescription;
}