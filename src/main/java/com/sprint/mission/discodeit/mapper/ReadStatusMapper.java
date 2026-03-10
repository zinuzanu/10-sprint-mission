package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {UserMapper.class, ChannelMapper.class})
public interface ReadStatusMapper {

  @Mapping(target = "channel", qualifiedByName = "toDtoWithLastMessage")
  ReadStatusDto toDto(ReadStatus entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "channel", ignore = true)
  @Mapping(target = "lastReadAt", ignore = true)
  ReadStatus toEntity(ReadStatusCreateRequest request);
}