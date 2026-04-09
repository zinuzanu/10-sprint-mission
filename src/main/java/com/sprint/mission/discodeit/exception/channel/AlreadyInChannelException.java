package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class AlreadyInChannelException extends ChannelException {

  public AlreadyInChannelException(UUID channelId, UUID userId) {
    super(ErrorCode.ALREADY_IN_CHANNEL, Map.of("channelId", channelId, "userId", userId));
  }
}
