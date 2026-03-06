package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {BinaryContentMapper.class, UserMapper.class})
public interface MessageMapper {

  MessageDto toDto(Message Entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "author", ignore = true)
  @Mapping(target = "channel", ignore = true)
  @Mapping(target = "attachments", ignore = true)
  Message toEntity(MessageCreateRequest request);
}
