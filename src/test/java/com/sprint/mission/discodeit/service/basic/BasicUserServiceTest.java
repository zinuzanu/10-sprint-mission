package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private ReadStatusRepository readStatusRepository;
  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private BasicUserService basicUserService;

  @Test
  @DisplayName("회원 가입 성공: 유효한 정보가 입력되면 새로운 사용자가 생성되어야 합니다.")
  void should_create_user() {
    // given
    UserCreateRequest request = new UserCreateRequest(
        "test@email.com",
        "test",
        "test1234");

    User user = new User(
        "test@email.com",
        "test",
        "test1234");

    UserDto userDto = UserDto.builder()
        .id(UUID.randomUUID())
        .email("test@email.com")
        .username("test")
        .build();

    given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());
    given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());
    given(userMapper.toEntity(any())).willReturn(user);
    given(userRepository.save(any())).willReturn(user);
    given(userMapper.toDto(any())).willReturn(userDto);

    // when
    UserDto result = basicUserService.create(request, null);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getEmail()).isEqualTo("test@email.com");
    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  @DisplayName("사용자 수정 성공: 올바른 요청 데이터가 전달되면 사용자 정보가 업데이트 되어야 합니다.")
  void should_update_user() {
    // given
    UUID userId = UUID.randomUUID();
    UserUpdateRequest request = new UserUpdateRequest(
        "newTest",
        "newTest@email.com",
        "newTest1234");

    User existingUser = new User(
        "test",
        "test@email.com",
        "test1234");

    UserDto expectedDto = UserDto.builder()
        .id(userId)
        .username("newTest")
        .email("newTest@email.com")
        .build();

    given(userRepository.findWithDetailsById(userId)).willReturn(Optional.of(existingUser));
    given(userRepository.findByEmail("newTest@email.com")).willReturn(Optional.empty());
    given(userMapper.toDto(any(User.class))).willReturn(expectedDto);

    // when
    UserDto result = basicUserService.update(userId, request, null);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getEmail()).isEqualTo("newTest@email.com");
    assertThat(result.getUsername()).isEqualTo("newTest");

    verify(userRepository).findWithDetailsById(userId);
  }

  @Test
  @DisplayName("사용자 삭제 실패: 존재하지 않는 사용자 ID인 경우 UserNotFoundException이 발생 되어야 합니다.")
  void should_throw_Exception_when_userNotFound() {
    // given
    UUID userId = UUID.randomUUID();

    given(userRepository.findWithDetailsById(userId)).willReturn(Optional.empty());

    // when, than
    assertThatThrownBy(() -> basicUserService.delete(userId))
        .isInstanceOf(UserNotFoundException.class);

    verify(userRepository, never()).delete(any(User.class));
    verify(readStatusRepository, never()).deleteByUser(any(User.class));
  }
}