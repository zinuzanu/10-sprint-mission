package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ChannelType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ChannelDto {

  public record CreatePublicRequest(
      @Schema(description = "채널 이름", example = "자바-스터디-방")
      String name,
      @Schema(description = "채널 설명", example = "자바 학습을 위한 공개 채널입니다.")
      String description
  ) {

  }

  public record CreatePrivateRequest(
      @Schema(description = "참여할 사용자 ID 리스트", example = "[\"550e8400-e29b-41d4-a716-446655440000\"]")
      List<UUID> participantIds
  ) {

  }

  public record UpdateRequest(
      @Schema(description = "변경할 채널 이름", example = "스프링-스터디-방")
      String newName,
      @Schema(description = "변경할 채널 설명", example = "스프링 프레임워크 학습용으로 변경합니다.")
      String newDescription
  ) {

  }

  public record Response(
      @Schema(description = "채널 ID", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
      UUID id,
      @Schema(description = "채널 이름", example = "자바-스터디-방")
      String name,
      @Schema(description = "채널 설명", example = "자바 학습을 위한 공개 채팅방입니다.")
      String description,
      @Schema(description = "채널 타입 (PUBLIC, PRIVATE)", example = "PUBLIC")
      ChannelType type,
      @Schema(description = "마지막 메시지 전송 시각", example = "2026-02-20T08:30:00Z")
      Instant lastMessageAt,
      @Schema(description = "채널 멤버 ID 리스트")
      List<UUID> memberIds
  ) {

  }
}
