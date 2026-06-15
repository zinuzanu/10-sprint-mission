package com.sprint.mission.discodeit.sse;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
public class SseService {

  private static final long TIMEOUT = 1000L * 60 * 60;

  private final Map<UUID, SseEmitter> emitters = new ConcurrentHashMap<>();

  public SseEmitter connect(UUID receiverId, UUID lastEventId) {

    SseEmitter emitter = new SseEmitter(TIMEOUT);

    emitters.put(receiverId, emitter);

    emitter.onCompletion(() -> emitters.remove(receiverId));
    emitter.onTimeout(() -> emitters.remove(receiverId));
    emitter.onError(e -> emitters.remove(receiverId));

    ping(emitter);

    log.info("[SUCCESS] SSE Connected: receiverId={}", receiverId);

    return emitter;
  }

  public void send(
      Collection<UUID> receiverIds,
      String eventName,
      Object data
  ) {

    receiverIds.forEach(receiverId -> {

      SseEmitter emitter = emitters.get(receiverId);

      if (emitter == null) {
        return;
      }

      try {

        emitter.send(
            SseEmitter.event()
                .name(eventName)
                .data(data)
        );

        log.info(
            "[SUCCESS] SSE Event Sent: receiverId={}, eventName={}",
            receiverId,
            eventName
        );

      } catch (IOException e) {

        emitters.remove(receiverId);

        log.warn(
            "[FAILED] SSE Event Send Failed: receiverId={}, eventName={}",
            receiverId,
            eventName
        );
      }
    });
  }

  public void broadcast(
      String eventName,
      Object data
  ) {

    emitters.forEach((receiverId, emitter) -> {

      try {

        emitter.send(
            SseEmitter.event()
                .name(eventName)
                .data(data)
        );

      } catch (IOException e) {

        emitters.remove(receiverId);

        log.warn(
            "[FAILED] SSE Broadcast Failed: receiverId={}, eventName={}",
            receiverId,
            eventName
        );
      }
    });
  }

  @Scheduled(fixedDelay = 1000 * 60 * 30)
  public void cleanUp() {

    int before = emitters.size();

    emitters.entrySet().removeIf(
        entry -> !ping(entry.getValue())
    );

    int removed = before - emitters.size();

    if (removed > 0) {
      log.info(
          "[SUCCESS] SSE Cleanup Completed: removedEmitters={}",
          removed
      );
    }
  }

  private boolean ping(SseEmitter sseEmitter) {

    try {

      sseEmitter.send(
          SseEmitter.event()
              .name("ping")
              .data("ping")
      );

      return true;

    } catch (IOException e) {

      return false;
    }
  }
}
