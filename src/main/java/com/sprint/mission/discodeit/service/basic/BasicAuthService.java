package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.AuthDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;

  @Override
  public AuthDto.LoginResponse login(AuthDto.LoginRequest request) {
    User user = userRepository.findByUsername(request.username())
        .orElseThrow(() -> new UserNotFoundException(request.username()));

    if (!user.getPassword().equals(request.password())) {
      throw new DiscodeitException(ErrorCode.LOGIN_FAILED);
    }

    return new AuthDto.LoginResponse(
        user.getId(),
        user.getUsername(),
        user.getEmail()
    );
  }
}
