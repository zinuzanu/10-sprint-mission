package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.ChannelCreatePublicRequest;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.time.Instant;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class ChannelMapper {

  @Autowired
  protected UserMapper userMapper;

  @Autowired
  protected MessageRepository messageRepository;

  // 1. 단일 엔티티 변환 (lastMessageAt 자동 조회)
  @Named("toDtoWithLastMessage")
  public ChannelDto toDto(Channel entity) {
    if (entity == null) {
      return null;
    }

    // DB에서 최신 메시지 시각 조회
    Instant lastMessageAt = messageRepository.findFirstByChannelOrderByCreatedAtDesc(entity)
        .map(Message::getCreatedAt)
        .orElse(null);

    return toDto(entity, entity.getParticipants(), lastMessageAt);
  }

  // 2. 수동 조립 메서드 (ChannelDto 생성자 순서에 맞춤)
  public ChannelDto toDto(Channel channel, List<User> participants, Instant lastMessageAt) {
    if (channel == null) {
      return null;
    }

    return new ChannelDto(
        channel.getId(),
        channel.getName(),
        channel.getDescription(),
        channel.getType(),
        lastMessageAt, // 드디어 값이 매핑됨
        participants.stream().map(userMapper::toDto).toList()
    );
  }

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "type", constant = "PUBLIC")
  public abstract Channel toEntity(ChannelCreatePublicRequest request);
}
