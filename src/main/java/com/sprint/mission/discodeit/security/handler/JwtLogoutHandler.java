package com.sprint.mission.discodeit.security.handler;

import com.sprint.mission.discodeit.entity.RefreshToken;
import com.sprint.mission.discodeit.security.jwt.JwtRegistry;
import com.sprint.mission.discodeit.service.auth.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

  private final JwtRegistry jwtRegistry;
  private final RefreshTokenService refreshTokenService;

  @Override
  public void logout(HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication) {

    if (request.getCookies() == null) {
      return;
    }

    for (Cookie cookie : request.getCookies()) {

      if (!"REFRESH_TOKEN".equals(cookie.getName())) {
        continue;
      }

      String refreshTokenValue = cookie.getValue();

      try {
        RefreshToken refreshToken =
            refreshTokenService.findByToken(refreshTokenValue);

        UUID userId = refreshToken.getUserId();

        jwtRegistry.invalidateJwtInformationByUserId(userId);

        refreshTokenService.delete(userId);

      } catch (Exception e) {
        System.err.println("Logout 실패: " + e.getMessage());
      }

      Cookie deleteCookie = new Cookie("REFRESH_TOKEN", null);
      deleteCookie.setHttpOnly(true);
      deleteCookie.setPath("/");
      deleteCookie.setMaxAge(0);

      response.addCookie(deleteCookie);
    }
  }
}
