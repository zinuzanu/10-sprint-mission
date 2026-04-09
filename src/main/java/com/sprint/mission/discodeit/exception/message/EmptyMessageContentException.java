package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class EmptyMessageContentException extends MessageException {

  public EmptyMessageContentException() {
    super(ErrorCode.EMPTY_MESSAGE_CONTENT);
  }
}
