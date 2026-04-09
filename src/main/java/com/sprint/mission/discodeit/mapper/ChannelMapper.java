package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.ChannelCreatePublicRequest;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ChannelMapper { // abstract class에서 interface로 변경 권장

  @Mapping(target = "participants", source = "participants")
  @Mapping(target = "lastMessageAt", source = "lastMessageAt")
  ChannelDto toDto(Channel channel, List<User> participants, Instant lastMessageAt);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "type", constant = "PUBLIC")
  Channel toEntity(ChannelCreatePublicRequest request);
}
