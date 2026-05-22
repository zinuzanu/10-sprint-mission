package com.sprint.mission.discodeit.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.JwtDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.service.auth.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.auth.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {

  private final ObjectMapper objectMapper;
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    DiscodeitUserDetails userDetails = (DiscodeitUserDetails) authentication.getPrincipal();
    UserDto userDto = userDetails.getUserDto();

    String email = userDto.getEmail();
    List<String> roles = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .toList();

    Map<String, Object> claims = new HashMap<>();
    claims.put("username", email);
    claims.put("roles", roles);

    String accessToken = jwtTokenProvider.generateAccessToken(claims, email);
    String refreshToken = jwtTokenProvider.generateRefreshToken(email);

    Cookie refreshTokenCookie = new Cookie("REFRESH_TOKEN", refreshToken);
    refreshTokenCookie.setHttpOnly(true);
    refreshTokenCookie.setPath("/");
    refreshTokenCookie.setMaxAge(420 * 60);
    response.addCookie(refreshTokenCookie);

    response.setStatus(HttpStatus.OK.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");

    JwtDto jwtDto = JwtDto.builder()
        .userDto(userDto)
        .accessToken(accessToken)
        .build();

    String jsonResponse = objectMapper.writeValueAsString(jwtDto);
    response.getWriter().write(jsonResponse);
  }
}
