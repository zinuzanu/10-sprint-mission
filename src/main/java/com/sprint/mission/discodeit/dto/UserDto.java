package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

public class UserDto {

  public record BinaryContentDto(
      @Schema(description = "파일명", example = "profile_image.png")
      String fileName,
      @Schema(description = "파일 데이터(바이너리)")
      byte[] data
  ) {

  }

  public record CreateRequest(
      @Schema(description = "사용자 이름", example = "username")
      String username,
      @Schema(description = "이메일 주소", example = "test@gmail.com")
      String email,
      @Schema(description = "비밀번호", example = "password1234")
      String password
  ) {

  }

  public record UpdateRequest(
      @Schema(description = "새로운 사용자 이름", example = "new_name")
      String newUsername,
      @Schema(description = "새로운 이메일 주소", example = "new_Test@gmail.com")
      String newEmail,
      @Schema(description = "새로운 비밀번호", example = "new_password1234")
      String newPassword
  ) {

  }

  public record Response(
      @Schema(description = "사용자 고유 ID", example = "550e8400-e29b-41d4-a716-446655440000")
      UUID id,
      @Schema(description = "생성 일시", example = "2026-02-20T07:23:52Z")
      Instant createdAt,
      @Schema(description = "수정 일시", example = "2026-02-20T08:23:52Z")
      Instant updatedAt,
      @Schema(description = "사용자 이름", example = "username")
      String username,
      @Schema(description = "이메일 주소", example = "test@gmail.com")
      String email,
      @Schema(description = "프로필 이미지 ID", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
      UUID profileId,
      @Schema(description = "온라인 접속 상태", example = "true")
      boolean online
  ) {

  }
}
