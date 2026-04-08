package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "바이너리 콘텐츠 생성 요청")
public class BinaryContentCreateRequest {

  @Schema(description = "파일명", example = "profile_image.png")
  @NotBlank(message = "파일명은 필수입니다.")
  private String fileName;

  @Schema(description = "파일 바이너리 데이터")
  @NotEmpty(message = "파일 데이터가 비어있을 수 없습니다.")
  private byte[] bytes;

  @Schema(description = "파일 크기 (bytes)", example = "102400")
  @NotNull(message = "파일 크기는 필수입니다.")
  private Long size;

  @Schema(description = "콘텐츠 타입", example = "image/png")
  @NotBlank(message = "콘텐츠 타입은 필수입니다.")
  private String contentType;

  @Schema(description = "생성 일시", example = "2026-02-20T17:20:00Z")
  @NotNull(message = "생성 일시는 필수입니다.")
  private Instant createdAt;
}