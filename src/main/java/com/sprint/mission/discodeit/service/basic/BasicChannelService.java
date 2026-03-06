package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelCreatePrivateRequest;
import com.sprint.mission.discodeit.dto.ChannelCreatePublicRequest;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.BusinessException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final ChannelMapper channelMapper;

  @Transactional
  @Override
  public ChannelDto createPublicChannel(ChannelCreatePublicRequest request) {
    Channel newChannel = channelMapper.toEntity(request);
    return channelMapper.toDto(channelRepository.save(newChannel));
  }

  @Transactional
  @Override
  public ChannelDto createPrivateChannel(ChannelCreatePrivateRequest request) {
    Channel newChannel = new Channel(null, null, ChannelType.PRIVATE);
    Channel saved = channelRepository.save(newChannel);

    request.getParticipantIds().forEach(userId -> {
      User user = findUserFromExistingReadStatus(userId);
      readStatusRepository.save(new ReadStatus(user, saved));
    });
    return channelMapper.toDto(saved);
  }

  @Override
  public ChannelDto findById(UUID id) {
    return channelMapper.toDto(findChannelEntityById(id));
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    return channelRepository.findAll().stream()
        .filter(c -> c.getType() == ChannelType.PUBLIC || isMember(userId, c.getId()))
        .map(channelMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public ChannelDto update(UUID channelId, ChannelUpdateRequest request) {
    Channel channel = findChannelEntityById(channelId);

    if (channel.getType() == ChannelType.PRIVATE) {
      throw new BusinessException(ErrorCode.PRIVATE_CHANNEL_NOT_UPDATABLE);
    }

    channel.update(
        request.getNewName(),
        request.getNewDescription()
    );
    return channelMapper.toDto(channel);
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
}
