package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 생성 요청")
public class UserCreateRequest {

  @Schema(description = "사용자 이름", example = "username")
  @NotBlank(message = "사용자 이름은 필수입니다.")
  @Size(max = 50, message = "이름은 50자를 초과할 수 없습니다.")
  private String username;

  @Schema(description = "이메일 주소", example = "test@gmail.com")
  @NotBlank(message = "이메일은 필수입니다.")
  @Email(message = "이메일 형식이 올바르지 않습니다.")
  @Size(max = 100)
  private String email;

  @Schema(description = "비밀번호", example = "password1234")
  @NotBlank(message = "비밀번호는 필수입니다.")
  @Size(min = 8, max = 60, message = "비밀번호는 8~60자 사이여야 합니다.")
  private String password;
}
