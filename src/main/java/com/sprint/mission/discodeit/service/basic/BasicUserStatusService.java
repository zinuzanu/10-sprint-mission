package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicUserStatusService implements UserStatusService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserStatusMapper userStatusMapper;

  @Transactional
  @Override
  public UserStatusDto create(UserStatusCreateRequest request) {
    if (userStatusRepository.existsByUserId(request.getUserId())) {
      throw new DiscodeitException(ErrorCode.USER_STATUS_ALREADY_EXISTS);
    }

    User user = findUserEntityById(request.getUserId());

    UserStatus userStatus = new UserStatus(user);
    return userStatusMapper.toDto(userStatusRepository.save(userStatus));
  }

  @Override
  public UserStatusDto findById(UUID id) {
    return userStatusMapper.toDto(findUserStatusEntityById(id));
  }

  @Override
  public List<UserStatusDto> findAllStatuses() {
    return userStatusRepository.findAllWithUser().stream()
        .map(userStatusMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public UserStatusDto update(UUID userId, UserStatusUpdateRequest request) {
    UserStatus userStatus = findUserStatusEntityByUserId(userId);
    userStatus.updateActiveTime();
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  @Override
  public void delete(UUID id) {
    UserStatus userStatus = findUserStatusEntityById(id);
    userStatusRepository.delete(userStatus);
  }

  // [헬퍼 메서드]: PK 조회
  private UserStatus findUserStatusEntityById(UUID id) {
    return userStatusRepository.findById(id)
        .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_STATUS_NOT_FOUND));
  }

  // [헬퍼 메서드]: userId(FK) 조회
  private UserStatus findUserStatusEntityByUserId(UUID userId) {
    return userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_STATUS_NOT_FOUND));
  }

  // [헬퍼 메서드]: User 조회
  private User findUserEntityById(UUID id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));
  }
}
