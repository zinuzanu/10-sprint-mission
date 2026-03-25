package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.BusinessException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final ReadStatusRepository readStatusRepository;
  private final UserMapper userMapper;

  @Transactional
  @Override
  public UserDto create(UserCreateRequest request, MultipartFile profile) {
    validateDuplicateEmail(request.getEmail());
    validateDuplicateUserName(request.getUsername());

    BinaryContent profileImage = processImage(null, profile);
    User newUser = userMapper.toEntity(request);

    if (profileImage != null) {
      newUser.update(null, null, null, profileImage);
    }

    UserStatus status = new UserStatus(newUser);
    newUser.setUserStatus(status);

    User saved = userRepository.save(newUser);

    log.info("[USER_CREATE_SUCCESS] 새로운 사용자 저장 완료: id={}, email={}", saved.getId(),
        saved.getEmail());

    return userMapper.toDto(saved);
  }

  @Override
  public UserDto findById(UUID id) {
    return userMapper.toDto(findUserEntityById(id));
  }

  @Override
  public List<UserDto> findAllUsers() {
    return userRepository.findAllWithDetails().stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Override
  public List<User> findAllByChannelId(UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      throw new BusinessException(ErrorCode.CHANNEL_NOT_FOUND);
    }
    return readStatusRepository.findAllByChannelId(channelId).stream()
        .map(ReadStatus::getUser)
        .toList();
  }

  @Transactional
  @Override
  public UserDto update(UUID userId, UserUpdateRequest request,
      MultipartFile profile) {
    User user = findUserEntityById(userId);

    if (request.getNewEmail() != null && !request.getNewEmail().equals(user.getEmail())) {
      validateDuplicateEmail(request.getNewEmail());
    }

    BinaryContent newProfile = processImage(user.getProfile(), profile);

    user.update(
        request.getNewUsername(),
        request.getNewEmail(),
        request.getNewPassword(),
        newProfile
    );

    log.info("[USER_UPDATE_SUCCESS] 사용자 정보 수정 완료: id={}, email={}", userId, user.getEmail());

    return userMapper.toDto(user);
  }

  @Transactional
  @Override
  public void delete(UUID userId) {

    User user = findUserEntityById(userId);

    readStatusRepository.deleteByUser(user);
    userRepository.delete(user);

    if (user.getProfile() != null) {
      binaryContentRepository.delete(user.getProfile());
    }

    log.info("[USER_DELETE_SUCCESS] 사용자 삭제 완료: id={}", userId);
  }

  // 이메일 중복 시 예외를 던져 가입 중단 (Fail-Fast)
  private void validateDuplicateEmail(String userEmail) {
    if (userRepository.findByEmail(userEmail).isPresent()) {
      throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
    }
  }

  // 이름 중복 시 예외를 던져 가입 중단 (Fail-Fast)
  private void validateDuplicateUserName(String username) {
    if (userRepository.findByUsername(username).isPresent()) {
      throw new BusinessException(ErrorCode.DUPLICATE_USERNAME);
    }
  }

  // [헬퍼 메서드]: 반복되는 조회 및 예외 처리 공통화
  private User findUserEntityById(UUID id) {
    return userRepository.findWithDetailsById(id)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
  }

  // [헬퍼 메서드]: 이미지 생성(createPublicChannel) 및 기존 이미지 수정(updateLastReadAt)
  private BinaryContent processImage(BinaryContent existingProfile, MultipartFile file) {
    if (file == null || file.isEmpty()) {
      return existingProfile;
    }

    // 기존 이미지가 있으면 삭제 (Update)
    if (existingProfile != null) {
      binaryContentRepository.delete(existingProfile);
    }

    try {
      // 새 이미지 저장
      BinaryContent newImage = new BinaryContent(
          file.getOriginalFilename(),
          file.getSize(),
          file.getContentType()
      );
      BinaryContent saved = binaryContentRepository.save(newImage);
      binaryContentStorage.put(saved.getId(), file.getBytes());
      return saved;
    } catch (IOException e) {
      throw new BusinessException(ErrorCode.FILE_SAVE_ERROR);
    }
  }
}
