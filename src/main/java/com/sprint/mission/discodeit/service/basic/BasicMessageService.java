package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.MessageDto.UpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.BusinessException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;

  @Transactional
  @Override
  public MessageDto.Response create(MessageDto.CreateRequest request,
      List<MultipartFile> attachments) {
    User author = userRepository.findById(request.authorId())
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    Channel channel = channelRepository.findById(request.channelId())
        .orElseThrow(() -> new BusinessException(ErrorCode.CHANNEL_NOT_FOUND));

    List<BinaryContent> attachmentContents = processAttachments(attachments);

    Message message = new Message(
        author,
        channel,
        request.content(),
        attachmentContents
    );
    return convertToResponse(messageRepository.save(message));
  }

  @Override
  public MessageDto.Response findById(UUID id) {
    return convertToResponse(findMessageEntityById(id));
  }

  @Override
  public List<MessageDto.Response> findAllByChannelId(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new BusinessException(ErrorCode.CHANNEL_NOT_FOUND));

    return messageRepository.findByChannel(channel).stream()
        .map(this::convertToResponse)
        .toList();
  }

  @Transactional
  @Override
  public MessageDto.Response update(UUID messageId, UpdateRequest request) {
    Message message = findMessageEntityById(messageId);

    message.update(request.newContent());
    return convertToResponse(message);
  }

  @Transactional
  @Override
  public void delete(UUID messageId) {
    Message message = findMessageEntityById(messageId);

    messageRepository.delete(message);
  }

  // [헬퍼 메서드]: 첨부 파일 처리
  private List<BinaryContent> processAttachments(List<MultipartFile> attachments) {
    List<BinaryContent> results = new ArrayList<>();
    if (attachments == null || attachments.isEmpty()) {
      return results;
    }

    for (MultipartFile file : attachments) {
      try {
        BinaryContent content = new BinaryContent(
            file.getOriginalFilename(),
            file.getSize(),
            file.getContentType(),
            file.getBytes()
        );
        results.add(binaryContentRepository.save(content));
      } catch (IOException e) {
        throw new BusinessException(ErrorCode.FILE_SAVE_ERROR);
      }
    }
    return results;
  }

  // [헬퍼 메서드]: 반복되는 조회 및 예외 처리 공통화
  private Message findMessageEntityById(UUID id) {
    return messageRepository.findById(id)
        .orElseThrow(() -> new BusinessException(ErrorCode.MESSAGE_NOT_FOUND));
  }

  // [헬퍼 메서드]: 요구사항에 맞는 Response DTO 변환
  private MessageDto.Response convertToResponse(Message message) {
    UUID authorId = (message.getAuthor() != null) ? message.getAuthor().getId() : null;
    return new MessageDto.Response(
        message.getId(),
        message.getContent(),
        authorId,
        message.getChannel().getId(),
        message.getCreatedAt(),
        message.getUpdatedAt(),
        message.getAttachments().stream().map(BinaryContent::getId).toList()
    );
  }
}
