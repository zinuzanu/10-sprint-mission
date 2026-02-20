package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.AuthDto;
import com.sprint.mission.discodeit.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 및 권한 관리 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @Operation(summary = "로그인", description = "사용자 이름과 비밀번호를 확인하여 사용자 정보를 반환합니다.")
  @PostMapping("/login")
  public AuthDto.LoginResponse login(@RequestBody AuthDto.LoginRequest request) {
    return authService.login(request);
  }
}
