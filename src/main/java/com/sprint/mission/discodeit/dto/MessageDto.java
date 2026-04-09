package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "메시지 정보 응답")
public class MessageDto {

  @Schema(description = "메시지 ID", example = "m1eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
  private UUID id;

  @Schema(description = "메시지 내용", example = "오늘 하루도 파이팅 합시다!!")
  private String content;

  @Schema(description = "작성자 정보")
  private UserDto author;

  @Schema(description = "채널 ID")
  private UUID channelId;

  @Schema(description = "생성 시각", example = "2026-02-20T08:30:00Z")
  private Instant createdAt;

  @Schema(description = "수정 시각", example = "2026-02-20T08:35:00Z")
  private Instant updatedAt;

  @Schema(description = "첨부 파일 리스트")
  private List<BinaryContentDto> attachments;
}
