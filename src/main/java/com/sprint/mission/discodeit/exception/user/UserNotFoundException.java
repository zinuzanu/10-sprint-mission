package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserNotFoundException extends UserException {

  public UserNotFoundException(UUID userId) {
    super(ErrorCode.USER_NOT_FOUND, Map.of("userId", userId));
  }

  public UserNotFoundException(String identifier) {
    super(ErrorCode.USER_NOT_FOUND, Map.of("identifier", identifier));
  }

  public UserNotFoundException(List<UUID> participantIds) {
    super(ErrorCode.USER_NOT_FOUND, Map.of("participantIds", participantIds));
  }

  public UserNotFoundException(Map<String, Object> details) {
    super(ErrorCode.USER_NOT_FOUND, details);
  }
}
