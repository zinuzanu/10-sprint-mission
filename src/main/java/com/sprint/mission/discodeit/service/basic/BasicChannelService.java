package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.BusinessException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;

  @Transactional
  @Override
  public ChannelDto.Response createPublicChannel(ChannelDto.CreatePublicRequest request) {
    Channel newChannel = new Channel(
        request.name(),
        request.description(),
        ChannelType.PUBLIC
    );
    return convertToResponse(channelRepository.save(newChannel));
  }

  @Transactional
  @Override
  public ChannelDto.Response createPrivateChannel(ChannelDto.CreatePrivateRequest request) {
    Channel newChannel = new Channel(
        null,
        null,
        ChannelType.PRIVATE
    );
    Channel saved = channelRepository.save(newChannel);

    request.participantIds().forEach(userId -> {
      User user = findUserFromExistingReadStatus(userId);
      readStatusRepository.save(new ReadStatus(user, saved));
    });
    return convertToResponse(saved);
  }

  @Override
  public ChannelDto.Response findById(UUID id) {
    return convertToResponse(findChannelEntityById(id));
  }

  @Override
  public List<ChannelDto.Response> findAllByUserId(UUID userId) {
    return channelRepository.findAll().stream()
        .filter(c -> c.getType() == ChannelType.PUBLIC || isMember(userId, c.getId()))
        .map(this::convertToResponse)
        .toList();
  }

  @Transactional
  @Override
  public ChannelDto.Response update(UUID channelId, ChannelDto.UpdateRequest request) {
    Channel channel = findChannelEntityById(channelId);

    if (channel.getType() == ChannelType.PRIVATE) {
      throw new BusinessException(ErrorCode.PRIVATE_CHANNEL_NOT_UPDATABLE);
    }

    channel.update(
        request.newName(),
        request.newDescription()
    );
    return convertToResponse(channel);
  }

  @Transactional
  @Override
  public void delete(UUID channelId) {
    Channel channel = findChannelEntityById(channelId);

    messageRepository.deleteByChannel(channel);
    readStatusRepository.deleteByChannel(channel);

    channelRepository.delete(channel);
  }

  // [헬퍼 메서드]: 유저 객체 발굴용 (가볍게 필터링된 메서드 활용)
  private User findUserFromExistingReadStatus(UUID userId) {
    return readStatusRepository.findAllByUserId(userId).stream()
        .findFirst()
        .map(ReadStatus::getUser)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
  }

  // [헬퍼 메서드]: 특정 유저의 채널 가입 여부를 확인
  private boolean isMember(UUID userId, UUID channelId) {
    return readStatusRepository.findAll().stream()
        .anyMatch(
            rs -> rs.getUser().getId().equals(userId) && rs.getChannel().getId().equals(channelId));
  }

  // [헬퍼 메서드]: 반복되는 조회 및 예외 처리 공통화
  private Channel findChannelEntityById(UUID id) {
    return channelRepository.findById(id)
        .orElseThrow(() -> new BusinessException(ErrorCode.CHANNEL_NOT_FOUND));
  }

  // [헬퍼 메서드]: 엔티티를 Response DTO로 변환 (가장 최신 메시지 시간 포함)
  private ChannelDto.Response convertToResponse(Channel channel) {
    // [추가] 요구사항: 해당 채널의 가장 최근 메시지 시간 정보 포함
    Instant lastMessageAt = messageRepository.findLatestMessageTimeByChannelId(channel.getId())
        .orElse(null);

    // [추가] 요구사항: PRIVATE인 경우 참여한 User ID 정보 포함
    List<UUID> participantIds = List.of();
    if (channel.getType() == ChannelType.PRIVATE) {
      participantIds = readStatusRepository.findAll().stream()
          .filter(rs -> rs.getChannel().getId().equals(channel.getId()))
          .map(rs -> rs.getUser().getId())
          .toList();
    }

    return new ChannelDto.Response(
        channel.getId(),
        channel.getName(),
        channel.getDescription(),
        channel.getType(),
        lastMessageAt,
        participantIds
    );
  }
}
