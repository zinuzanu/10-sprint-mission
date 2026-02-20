package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

public class BinaryContentDto {

  public record CreateRequest(
      @Schema(description = "파일명", example = "profile_image.png")
      String fileName,
      @Schema(description = "파일 바이너리 데이터")
      byte[] bytes,
      @Schema(description = "파일 크기 (bytes)", example = "102400")
      Long size,
      @Schema(description = "콘텐츠 타입", example = "image/png")
      String contentType,
      @Schema(description = "생성 일시", example = "2026-02-20T17:20:00Z")
      Instant createdAt
  ) {

  }

  public record Response(
      @Schema(description = "바이너리 콘텐츠 ID", example = "b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
      UUID id,
      @Schema(description = "생성 일시", example = "2026-02-20T17:20:00Z")
      Instant createdAt,
      @Schema(description = "파일명", example = "profile_image.png")
      String fileName,
      @Schema(description = "파일 크기 (bytes)", example = "102400")
      Long size,
      @Schema(description = "콘텐츠 타입", example = "image/png")
      String contentType,
      @Schema(description = "파일 바이너리 데이터")
      byte[] bytes
  ) {

  }
}
