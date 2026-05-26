package com.sprint.mission.discodeit.config.security;

import com.sprint.mission.discodeit.entity.RefreshToken;
import com.sprint.mission.discodeit.service.auth.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

  private final RefreshTokenService refreshTokenService;

  @Override
  public void logout(HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication) {

    Cookie[] cookies = request.getCookies();

    if (cookies == null) {
      return;
    }

    for (Cookie cookie : cookies) {

      if (!"REFRESH_TOKEN".equals(cookie.getName())) {
        continue;
      }

      String refreshTokenValue = cookie.getValue();

      try {
        RefreshToken refreshToken =
            refreshTokenService.findByToken(refreshTokenValue);

        refreshTokenService.delete(refreshToken.getUserId());

      } catch (Exception e) {
      }

      Cookie deleteCookie = new Cookie("REFRESH_TOKEN", null);
      deleteCookie.setHttpOnly(true);
      deleteCookie.setPath("/");
      deleteCookie.setMaxAge(0);

      response.addCookie(deleteCookie);
    }
  }
}
