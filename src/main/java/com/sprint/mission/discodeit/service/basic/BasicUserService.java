package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.BusinessException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.UserService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final ReadStatusRepository readStatusRepository;

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
    userRepository.save(newUser);

    if (userStatusRepository != null) {
      UserStatus status = new UserStatus(newUser);
      userStatusRepository.save(status);
    }

    return convertToResponse(newUser);
  }

  @Override
  public UserDto.Response findById(UUID id) {
    User user = findUserEntityById(id);
    return convertToResponse(user);
  }

  @Override
  public List<UserDto.Response> findAll() {
    return userRepository.findAll().stream()
        .map(this::convertToResponse)
        .collect(Collectors.toList());
  }

  @Override
  public List<User> findUsersByChannelId(UUID channelId) {
    List<UUID> memberIds = channelRepository.findById(channelId)
        .map(Channel::getMemberIds)
        .orElseThrow(() -> new BusinessException(ErrorCode.CHANNEL_NOT_FOUND));
    return memberIds.stream()
        .map(this::findUserEntityById)
        .collect(Collectors.toList());
  }

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
        newProfile);

    userRepository.save(user);
    return convertToResponse(user);
  }

  @Override
  public void delete(UUID userId) {
    // 0. 삭제 대상 조회
    User user = findUserEntityById(userId);

    // 1. 유저가 작성한 메시지 삭제
    messageRepository.findAll().stream()
        .filter(m -> user.equals(m.getAuthor()))
        .forEach(m -> messageRepository.deleteById(m.getId()));

    // 2. 채널에서 유저 제거
    channelRepository.findAll().forEach(channel -> {
      if (channel.getMemberIds().contains(userId)) {
        channel.removeMember(userId);
        channelRepository.save(channel);
      }
    });

    // 2-1. 유저의 채널 참여 정보(ReadStatus) 삭제
    readStatusRepository.findAll().stream()
        .filter(rs -> user.equals(rs.getUser()))
        .forEach(rs -> readStatusRepository.deleteById(rs.getId()));

    // 3. 바이너리 파일 삭제 (프로필 이미지)
    if (user.getProfile() != null && binaryContentRepository != null) {
      binaryContentRepository.deleteById(user.getProfile().getId());
    }

    // 4. 유저 상태 정보 삭제
    if (userStatusRepository != null) {
      userStatusRepository.findAll().stream()
          .filter(us -> user.equals(us.getUser()))
          .findFirst()
          .ifPresent(us ->
              userStatusRepository.deleteById(us.getId()));
    }

    // 5. 유저 삭제
    userRepository.deleteById(userId);
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
    if (userStatusRepository != null) {
      online = userStatusRepository.findById(user.getId())
          .map(UserStatus::isOnline)
          .orElse(false);
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
      binaryContentRepository.deleteById(existingProfile.getId());
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
