package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 수정 요청")
public class UserUpdateRequest {

  @Schema(description = "새로운 사용자 이름", example = "new_name")
  private String newUsername;

  @Schema(description = "새로운 이메일 주소", example = "new_Test@gmail.com")
  private String newEmail;

  @Schema(description = "새로운 비밀번호", example = "new_password1234")
  private String newPassword;
}
