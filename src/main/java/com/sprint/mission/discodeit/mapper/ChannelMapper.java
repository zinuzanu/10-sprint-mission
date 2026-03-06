package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.ChannelCreatePublicRequest;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {UserMapper.class})
public interface ChannelMapper {

  ChannelDto toDto(Channel entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "type", constant = "PUBLIC")
  Channel toEntity(ChannelCreatePublicRequest request);
}
