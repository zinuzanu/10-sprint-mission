package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "바이너리 콘텐츠 생성 요청")
public class BinaryContentCreateRequest {

  @Schema(description = "파일명", example = "profile_image.png")
  private String fileName;

  @Schema(description = "파일 바이너리 데이터")
  private byte[] bytes;

  @Schema(description = "파일 크기 (bytes)", example = "102400")
  private Long size;

  @Schema(description = "콘텐츠 타입", example = "image/png")
  private String contentType;

  @Schema(description = "생성 일시", example = "2026-02-20T17:20:00Z")
  private Instant createdAt;
}