package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class InvalidChannelNameException extends ChannelException {

  public InvalidChannelNameException(String channelName) {
    super(ErrorCode.INVALID_CHANNEL_NAME, Map.of("channelName", channelName));
  }
}
