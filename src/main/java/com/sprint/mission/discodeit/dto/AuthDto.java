package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public class AuthDto {

  public record LoginRequest(
      @Schema(description = "사용자 이름", example = "username")
      String username,
      @Schema(description = "비밀번호", example = "password1234")
      String password
  ) {

  }

  public record LoginResponse(
      @Schema(description = "사용자 ID", example = "550e8400-e29b-41d4-a716-446655440000")
      UUID id,
      @Schema(description = "사용자 이름", example = "username")
      String username,
      @Schema(description = "이메일 주소", example = "test@gmail.com")
      String email
  ) {

  }
}
