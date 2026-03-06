package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 생성 요청")
public class UserCreateRequest {

  @Schema(description = "사용자 이름", example = "username")
  private String username;

  @Schema(description = "이메일 주소", example = "test@gmail.com")
  private String email;

  @Schema(description = "비밀번호", example = "password1234")
  private String password;
}
