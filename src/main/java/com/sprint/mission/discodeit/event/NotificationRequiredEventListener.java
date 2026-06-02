package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationRequiredEventListener {

  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
  public void on(MessageCreatedEvent event) {

    try {
      Message message = messageRepository.findWithAuthorAndChannelById(event.messageId())
          .orElseThrow();

      Channel channel = message.getChannel();

      List<ReadStatus> targets =
          readStatusRepository.findAllByChannelIdAndNotificationEnabledTrueAndUserIdNot(
              channel.getId(),
              message.getAuthor().getId()
          );

      List<Notification> notifications = targets.stream()
          .map(rs -> new Notification(
              rs.getUser(),
              "보낸 사람 (" + channel.getName() + ")",
              message.getContent()
          ))
          .toList();

      notificationRepository.saveAll(notifications);

      log.info("[SUCCESS] Message Notification Created: messageId={}", event.messageId());

    } catch (Exception e) {
      log.error("[FAIL] Message Notification Failed: messageId={}", event.messageId(), e);
    }
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
  public void on(RoleUpdatedEvent event) {

    try {
      Notification notification = new Notification(
          userRepository.findById(event.userId()).orElseThrow(),
          "권한이 변경되었습니다.",
          event.previousRole() + " -> " + event.newRole()
      );

      notificationRepository.save(notification);

      log.info("[SUCCESS] Role Notification Created: userId={}", event.userId());

    } catch (Exception e) {
      log.error("[FAIL] Role Notification Failed: userId={}", event.userId(), e);
    }
  }
}
