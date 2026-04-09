package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {UserMapper.class})
public interface UserStatusMapper {

  @Mapping(target = "id", source = "entity.id")
  @Mapping(target = "user", source = "entity.user")
  @Mapping(target = "lastOnlineAt", source = "entity.lastActiveAt")
  @Mapping(target = "isOnline", expression = "java(entity.isOnline())")
  UserStatusDto toDto(UserStatus entity);
}