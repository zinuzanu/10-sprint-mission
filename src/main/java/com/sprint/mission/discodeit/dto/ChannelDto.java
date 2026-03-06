package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ChannelType;
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
@Schema(description = "채널 정보 응답")
public class ChannelDto {

  @Schema(description = "채널 ID", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
  private UUID id;

  @Schema(description = "채널 이름", example = "자바-스터디-방")
  private String name;

  @Schema(description = "채널 설명", example = "자바 학습을 위한 공개 채팅방입니다.")
  private String description;

  @Schema(description = "채널 타입 (PUBLIC, PRIVATE)", example = "PUBLIC")
  private ChannelType type;

  @Schema(description = "마지막 메시지 전송 시각", example = "2026-02-20T08:30:00Z")
  private Instant lastMessageAt;

  @Schema(description = "채널 멤버 리스트")
  private List<UserDto> participants;
}
