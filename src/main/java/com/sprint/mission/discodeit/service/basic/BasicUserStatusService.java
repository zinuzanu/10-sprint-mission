package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.dto.UserStatusDto.UpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.BusinessException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  public UserStatusDto.Response create(UserStatusDto.CreateRequest request) {
    User user = userRepository.findById(request.userID())
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

    boolean exists = userStatusRepository.findAll().stream()
        .anyMatch(us -> us.getUser().getId().equals(request.userID()));
    if (exists) {
      throw new BusinessException(ErrorCode.USER_STATUS_ALREADY_EXISTS);
    }
    UserStatus userStatus = new UserStatus(user, request.lastOnlineAt());
    return convertToResponse(userStatusRepository.save(userStatus));
  }

  @Override
  public UserStatusDto.Response findById(UUID id) {
    return convertToResponse(findUserStatusEntityById(id));
  }

  @Override
  public List<UserStatusDto.Response> findAll() {
    return userStatusRepository.findAll().stream()
        .map(this::convertToResponse)
        .toList();
  }

  @Override
  public UserStatusDto.Response update(UUID userId, UpdateRequest request) {
    return updateByUserId(userId, request.newLastActiveAt());
  }

  @Override
  public UserStatusDto.Response updateByUserId(UUID userId, Instant lastOnlineAt) {
    UserStatus userStatus = userStatusRepository.findAll().stream()
        .filter(us -> us.getUser().getId().equals(userId))
        .findFirst()
        .orElseGet(() -> {
          User user = userRepository.findById(userId)
              .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
          return userStatusRepository.save(new UserStatus(user, Instant.MIN));
        });

    userStatus.update(lastOnlineAt);
    return convertToResponse(userStatusRepository.save(userStatus));
  }

  @Override
  public void delete(UUID id) {
    findUserStatusEntityById(id);
    userStatusRepository.deleteById(id);
  }

  // [헬퍼 메서드]: 반복되는 조회 및 예외 처리 공통화
  private UserStatus findUserStatusEntityById(UUID id) {
    return userStatusRepository.findById(id)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_STATUS_NOT_FOUND));
  }

  private UserStatusDto.Response convertToResponse(UserStatus userStatus) {
    return new UserStatusDto.Response(
        userStatus.getId(),
        userStatus.getUser().getId(),
        userStatus.getLastActiveAt(),
        userStatus.isOnline()
    );
  }
}
