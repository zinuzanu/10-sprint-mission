package com.sprint.mission.discodeit.sse;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SseMessage {

  private UUID id;
  private String eventName;
  private Object data;
}
