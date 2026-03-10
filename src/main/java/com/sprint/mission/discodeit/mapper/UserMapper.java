package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {BinaryContentMapper.class})
public interface UserMapper {

  @Mapping(target = "online", source = "status")
  UserDto toDto(User Entity);

  default boolean mapOnlineStatus(UserStatus status) {
    if (status == null) {
      return false;
    }
    return status.isOnline(); // 엔티티에 이미 만들어두신 로직 호출
  }

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "profile", ignore = true)
  User toEntity(UserCreateRequest request);
}
