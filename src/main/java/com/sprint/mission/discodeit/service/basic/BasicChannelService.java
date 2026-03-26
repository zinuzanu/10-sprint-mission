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
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelNotUpdatableException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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

    log.info("[CHANNEL_CREATE_PUBLIC_SUCCESS] 공개 채널 저장 완료: id={}, name={}",
        savedPublicChannel.getId(), newChannel.getName());

    return channelMapper.toDto(savedPublicChannel, List.of(), null);
  }

  @Transactional
  @Override
  public ChannelDto createPrivateChannel(ChannelCreatePrivateRequest request) {
    // 1. 참여자 리스트를 상세 정보와 함께 일괄 조회 (N+1 방지 및 profile null 방지)
    List<User> participants = userRepository.findAllWithDetailsByIdIn(request.getParticipantIds());

    if (participants.size() != request.getParticipantIds().size()) {
      throw new UserNotFoundException(request.getParticipantIds());
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

    log.info("[CHANNEL_CREATE_PRIVATE_SUCCESS] 비공개 채널 저장 완료: id={}, participantCount={}",
        savedChannel.getId(), participants.size());

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
      throw new PrivateChannelNotUpdatableException(channelId);
    }

    channel.update(request.getNewName(), request.getNewDescription());

    Instant lastMessageAt = messageRepository.findFirstByChannelOrderByCreatedAtDesc(channel)
        .map(Message::getCreatedAt)
        .orElse(null);

    log.info("[CHANNEL_UPDATE_SUCCESS] 채널 정보 수정 완료: id={}", channelId);

    return channelMapper.toDto(channel, channel.getParticipants(), lastMessageAt);
  }

  @Transactional
  @Override
  public void delete(UUID channelId) {
    Channel channel = findChannelEntityById(channelId);

    messageRepository.deleteByChannel(channel);
    readStatusRepository.deleteByChannel(channel);

    channelRepository.delete(channel);

    log.info("[CHANNEL_DELETE_SUCCESS] 채널 삭제 완료: id={}", channelId);
  }

  // [헬퍼 메서드]: 반복되는 조회 및 예외 처리 공통화
  private Channel findChannelEntityById(UUID id) {
    return channelRepository.findById(id)
        .orElseThrow(() -> new ChannelNotFoundException(id));
  }
}
