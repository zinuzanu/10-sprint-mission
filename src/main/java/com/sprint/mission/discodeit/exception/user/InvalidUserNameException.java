package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class InvalidUserNameException extends UserException {

  public InvalidUserNameException(String username) {
    super(ErrorCode.INVALID_USERNAME, Map.of("username", username));
  }
}
