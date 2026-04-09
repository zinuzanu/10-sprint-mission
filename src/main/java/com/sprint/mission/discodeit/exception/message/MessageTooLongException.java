package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class MessageTooLongException extends MessageException {

  public MessageTooLongException(int length) {
    super(ErrorCode.MESSAGE_TOO_LONG, Map.of("length", length));
  }
}
