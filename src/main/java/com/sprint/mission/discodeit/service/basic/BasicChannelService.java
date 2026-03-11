package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelCreatePrivateRequest;
import com.sprint.mission.discodeit.dto.ChannelCreatePublicRequest;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.BusinessException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
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
  private final UserRepository userRepository;
  private final ChannelMapper channelMapper;

  @Transactional
  @Override
  public ChannelDto createPublicChannel(ChannelCreatePublicRequest request) {
    Channel newChannel = channelMapper.toEntity(request);
    Channel savedPublicChannel = channelRepository.save(newChannel);
    return channelMapper.toDto(savedPublicChannel, List.of(), null);
  }

  @Transactional
  @Override
  public ChannelDto createPrivateChannel(ChannelCreatePrivateRequest request) {
    // 1. 참여자 리스트를 상세 정보와 함께 일괄 조회 (N+1 방지 및 profile null 방지)
    List<User> participants = userRepository.findAllWithDetailsByIdIn(request.getParticipantIds());

    if (participants.size() != request.getParticipantIds().size()) {
      throw new BusinessException(ErrorCode.USER_NOT_FOUND);
    }

    // 2. 신규 비공개 채널 생성
    Channel newChannel = new Channel(null, null, ChannelType.PRIVATE);

    // 3. 관계 설정 (CascadeType.ALL 설정이 되어 있으므로 addReadStatus만 수행)
    participants.forEach(user -> {
      ReadStatus rs = new ReadStatus(user, newChannel);
      newChannel.addReadStatus(rs);
    });

    // 4. 저장 및 DTO 변환 (ChannelMapper가 UserMapper를 통해 완전한 UserDto를 생성)
    Channel savedChannel = channelRepository.save(newChannel);
    return channelMapper.toDto(savedChannel, participants, null);
  }

  @Override
  public ChannelDto findById(UUID channelId) {
    Channel channel = findChannelEntityById(channelId);

    // 단건 조회 시: 이 채널의 최신 메시지 시각을 레포지토리에서 딱 한 번 조회
    Instant lastMessageAt = messageRepository.findFirstByChannelOrderByCreatedAtDesc(channel)
        .map(Message::getCreatedAt)
        .orElse(null);

    return channelMapper.toDto(channel, channel.getParticipants(), lastMessageAt);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChannelDto> findAllByUserId(UUID userId) {
// 1. 내 채널들을 가져온다.
    List<Channel> channels = channelRepository.findAllVisibleChannelsWithParticipants(userId);

    // 2. 레포지토리에서 (채널ID, 최신시각) 묶음들을 다 가져온다.
    List<Object[]> lastMessageData = messageRepository.findAllLastMessageAt();

    // 3. 찾기 쉽게 맵으로 변환한다. (채널ID -> 시각)
    Map<UUID, Instant> lastMessageMap = lastMessageData.stream()
        .collect(Collectors.toMap(
            obj -> (UUID) obj[0],
            obj -> (Instant) obj[1],
            (existing, replacement) -> existing // 중복 시 기존값 유지
        ));

    // 4. 매퍼한테 재료를 다 던져준다. (N+1 없음!)
    return channels.stream()
        .map(channel -> channelMapper.toDto(
            channel,
            channel.getParticipants(),
            lastMessageMap.get(channel.getId()) // 맵에서 꺼내면 끝!
        ))
        .toList();
  }

  @Transactional
  @Override
  public ChannelDto update(UUID channelId, ChannelUpdateRequest request) {
    Channel channel = findChannelEntityById(channelId);

    if (channel.getType() == ChannelType.PRIVATE) {
      throw new BusinessException(ErrorCode.PRIVATE_CHANNEL_NOT_UPDATABLE);
    }

    channel.update(request.getNewName(), request.getNewDescription());

    Instant lastMessageAt = messageRepository.findFirstByChannelOrderByCreatedAtDesc(channel)
        .map(Message::getCreatedAt)
        .orElse(null);

    return channelMapper.toDto(channel, channel.getParticipants(), lastMessageAt);
  }

  @Transactional
  @Override
  public void delete(UUID channelId) {
    Channel channel = findChannelEntityById(channelId);

    messageRepository.deleteByChannel(channel);
    readStatusRepository.deleteByChannel(channel);

    channelRepository.delete(channel);
  }

  // [헬퍼 메서드]: 반복되는 조회 및 예외 처리 공통화
  private Channel findChannelEntityById(UUID id) {
    return channelRepository.findById(id)
        .orElseThrow(() -> new BusinessException(ErrorCode.CHANNEL_NOT_FOUND));
  }
}
