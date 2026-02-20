package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  UserDto.Response create(UserDto.CreateRequest request, MultipartFile profile);

  UserDto.Response findById(UUID id);

  List<UserDto.Response> findAll();

  List<User> findUsersByChannelId(UUID channelId);

  UserDto.Response update(UUID userId, UserDto.UpdateRequest request, MultipartFile profile);

  void delete(UUID id);
}
