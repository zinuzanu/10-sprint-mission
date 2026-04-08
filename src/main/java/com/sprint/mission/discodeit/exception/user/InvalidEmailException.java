package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class InvalidEmailException extends UserException {

  public InvalidEmailException(String email) {
    super(ErrorCode.INVALID_EMAIL, Map.of("email", email));
  }
}
