package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.MessageDto.UpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.BusinessException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  public MessageDto.Response create(MessageDto.CreateRequest request,
      List<MultipartFile> attachments) {
    if (!userRepository.existsById(request.authorId())) {
      throw new BusinessException(ErrorCode.USER_NOT_FOUND);
    }
    if (!channelRepository.existsById(request.channelId())) {
      throw new BusinessException(ErrorCode.CHANNEL_NOT_FOUND);
    }

    List<UUID> attachmentIds = new ArrayList<>();
    if (attachments != null && binaryContentRepository != null) {
      attachments.forEach(file -> {
        try {
          BinaryContent content = new BinaryContent(
              UUID.randomUUID(),
              file.getOriginalFilename(),
              file.getBytes(),
              Instant.now()
          );
          binaryContentRepository.save(content);
          attachmentIds.add(content.getId());
        } catch (IOException e) {
          throw new BusinessException(ErrorCode.FILE_SAVE_ERROR);
        }
      });
    }

    Message message = new Message(
        request.authorId(),
        request.channelId(),
        request.content(),
        attachmentIds
    );
    return convertToResponse(messageRepository.save(message));
  }

  @Override
  public MessageDto.Response findById(UUID id) {
    Message message = findMessageEntityById(id);
    return convertToResponse(message);
  }

  @Override
  public List<MessageDto.Response> findAllByChannelId(UUID channelId) {
    return messageRepository.findAll().stream()
        .filter(m -> m.getChannelId().equals(channelId))
        .map(this::convertToResponse)
        .toList();
  }

  @Override
  public MessageDto.Response update(UUID messageId, UpdateRequest request) {
    Message message = findMessageEntityById(messageId);

    message.update(request.newContent());
    return convertToResponse(messageRepository.save(message));
  }

  @Override
  public void delete(UUID messageId) {
    Message message = findMessageEntityById(messageId);

    if (binaryContentRepository != null && message.getAttachmentIds() != null) {
      message.getAttachmentIds().forEach(binaryContentRepository::deleteById);
    }
    messageRepository.deleteById(messageId);
  }

  // [헬퍼 메서드]: 반복되는 조회 및 예외 처리 공통화
  private Message findMessageEntityById(UUID id) {
    return messageRepository.findById(id)
        .orElseThrow(() -> new BusinessException(ErrorCode.MESSAGE_NOT_FOUND));
  }

  // [헬퍼 메서드]: 요구사항에 맞는 Response DTO 변환
  private MessageDto.Response convertToResponse(Message message) {
    return new MessageDto.Response(
        message.getId(),
        message.getContent(),
        message.getAuthorId(),
        message.getChannelId(),
        message.getCreatedAt(),
        message.getUpdatedAt(),
        message.getAttachmentIds()
    );
  }
}
