package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.BusinessException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final ReadStatusRepository readStatusRepository;

  @Transactional
  @Override
  public UserDto.Response create(UserDto.CreateRequest request, MultipartFile profile) {
    validateDuplicateEmail(request.email());
    validateDuplicateUserName(request.username());

    BinaryContent profileImage = processImage(null, profile);

    User newUser = new User(
        request.username(),
        request.email(),
        request.password()
    );

    if (profileImage != null) {
      newUser.update(null, null, null, profileImage);
    }

    new UserStatus(newUser);

    userRepository.save(newUser);
    return convertToResponse(newUser);
  }

  @Override
  public UserDto.Response findById(UUID id) {
    return convertToResponse(findUserEntityById(id));
  }

  @Override
  public List<UserDto.Response> findAll() {
    return userRepository.findAll().stream()
        .map(this::convertToResponse)
        .collect(Collectors.toList());
  }

  @Override
  public List<User> findUsersByChannelId(UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      throw new BusinessException(ErrorCode.CHANNEL_NOT_FOUND);
    }
    return readStatusRepository.findAllByChannelId(channelId).stream()
        .map(ReadStatus::getUser)
        .toList();
  }

  @Transactional
  @Override
  public UserDto.Response update(UUID userId, UserDto.UpdateRequest request,
      MultipartFile profile) {
    User user = findUserEntityById(userId);

    if (request.newEmail() != null && !request.newEmail().equals(user.getEmail())) {
      validateDuplicateEmail(request.newEmail());
    }

    BinaryContent newProfile = processImage(user.getProfile(), profile);

    user.update(
        request.newUsername(),
        request.newEmail(),
        request.newPassword(),
        newProfile
    );
    return convertToResponse(user);
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
    return userRepository.findById(id)
        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
  }

  // [헬퍼 메서드]: 엔티티를 클라이언트 응답용 DTO로 변환 및 데이터 가공
  private UserDto.Response convertToResponse(User user) {
    boolean online = false;
    if (user.getStatus() != null) {
      online = user.getStatus().isOnline();
    }
    return new UserDto.Response(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(),
        user.getProfile() != null ? user.getProfile().getId() : null,
        online
    );
  }

  // [헬퍼 메서드]: 이미지 생성(createPublicChannel) 및 기존 이미지 수정(updateLastReadAt)
  private BinaryContent processImage(BinaryContent existingProfile, MultipartFile file) {
    if (file == null || binaryContentRepository == null) {
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
          file.getContentType(),
          file.getBytes()
      );
      return binaryContentRepository.save(newImage);
    } catch (IOException e) {
      throw new BusinessException(ErrorCode.FILE_SAVE_ERROR);
    }
  }
}
