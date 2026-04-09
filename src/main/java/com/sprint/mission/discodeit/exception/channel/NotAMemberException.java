package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class NotAMemberException extends ChannelException {

  public NotAMemberException(UUID channelId, UUID userId) {
    super(ErrorCode.NOT_A_MEMBER, Map.of("channelId", channelId, "userId", userId));
  }
}
