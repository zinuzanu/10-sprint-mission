package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.BusinessException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
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
      throw new BusinessException(ErrorCode.USER_STATUS_ALREADY_EXISTS);
    }

    User user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

    UserStatus userStatus = new UserStatus(user);
    return userStatusMapper.toDto(userStatusRepository.save(userStatus));
  }

  @Override
  public UserStatusDto findById(UUID id) {
    return userStatusMapper.toDto(findUserStatusEntityById(id));
  }

  @Override
  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAll().stream()
        .map(userStatusMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public UserStatusDto update(UUID userId, UserStatusUpdateRequest request) {
    return updateByUserId(userId, request.getNewLastActiveAt());
  }

  @Transactional
  @Override
  public UserStatusDto updateByUserId(UUID userId, Instant lastOnlineAt) {
    UserStatus userStatus = getOrCreateUserStatus(userId);
    userStatus.updateActiveTime();
    return userStatusMapper.toDto(userStatusRepository.save(userStatus));
  }

  @Transactional
  @Override
  public void delete(UUID id) {
    UserStatus userStatus = findUserStatusEntityById(id);
    userStatusRepository.delete(userStatus);
  }

  // [헬퍼 메서드]: 반복되는 조회 및 예외 처리 공통화
  private UserStatus findUserStatusEntityById(UUID id) {
    return userStatusRepository.findById(id)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_STATUS_NOT_FOUND));
  }

  // [헬퍼 메서드]: 유저 상태의 존재 여부에 따른 조회/생성 로직
  private UserStatus getOrCreateUserStatus(UUID userId) {
    return userStatusRepository.findByUserId(userId)
        .orElseGet(() -> {
          User user = userRepository.findById(userId)
              .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
          return userStatusRepository.save(new UserStatus(user));
        });
  }
}
