package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class MessageDto {

  public record CreateRequest(
      @Schema(description = "작성자 ID", example = "550e8400-e29b-41d4-a716-446655440000")
      UUID authorId,
      @Schema(description = "채널 ID", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
      UUID channelId,
      @Schema(description = "메시지 내용", example = "오늘 하루도 파이팅 합시다!!")
      String content
  ) {

  }

  public record UpdateRequest(
      @Schema(description = "수정할 메시지 내용", example = "오늘 날씨가 춥네요!!")
      String newContent
  ) {

  }

  public record Response(
      @Schema(description = "메시지 ID", example = "m1eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
      UUID id,
      @Schema(description = "메시지 내용", example = "오늘 하루도 파이팅 합시다!!")
      String content,
      @Schema(description = "작성자 ID", example = "550e8400-e29b-41d4-a716-446655440000")
      UUID authorId,
      @Schema(description = "채널 ID", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
      UUID channelId,
      @Schema(description = "생성 시각", example = "2026-02-20T08:30:00Z")
      Instant createdAt,
      @Schema(description = "수정 시각", example = "2026-02-20T08:35:00Z")
      Instant updatedAt,
      @Schema(description = "첨부 파일 ID 리스트")
      List<UUID> attachmentIds
  ) {

  }
}
