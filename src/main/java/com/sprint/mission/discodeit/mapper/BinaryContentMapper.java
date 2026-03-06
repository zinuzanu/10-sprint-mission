package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BinaryContentMapper {

  BinaryContentDto toDto(BinaryContent entity);

  @Mapping(target = "id", ignore = true)
  BinaryContent toEntity(BinaryContentCreateRequest request);
}
