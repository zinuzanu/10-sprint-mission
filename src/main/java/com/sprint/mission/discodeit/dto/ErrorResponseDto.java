package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "공통 예외 응답")
public class ErrorResponseDto {

  @Schema(description = "예외 발생 시각", example = "2026-03-26T11:45:00Z")
  private Instant timestamp;

  @Schema(description = "비즈니스 에러 코드", example = "U304")
  private String code;

  @Schema(description = "에러 메시지", example = "존재하지 않은 사용자 입니다.")
  private String message;

  @Schema(description = "상세 에러 정보 (ID, 입력값 등)")
  private Map<String, Object> details;

  @Schema(description = "발생한 예외 클래스 이름", example = "UserNotFoundException")
  private String exceptionType;

  @Schema(description = "HTTP 상태 코드", example = "400")
  private int status;

  @Schema(description = "요청 경로", example = "/api/v1/users/123")
  private String path;
}
