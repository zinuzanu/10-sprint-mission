package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sprint.mission.discodeit.dto.ChannelCreatePrivateRequest;
import com.sprint.mission.discodeit.dto.ChannelCreatePublicRequest;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasicChannelServiceTest {

  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private ChannelMapper channelMapper;

  @InjectMocks
  private BasicChannelService basicChannelService;

  @Test
  @DisplayName("공개 채널 생성 성공: 유효한 정보가 입력되면 공개 채널이 생성되어야 합니다.")
  void should_create_public_channel() {
    // given
    ChannelCreatePublicRequest request = new ChannelCreatePublicRequest(
        "진우의 공개 채널",
        "안녕하세요. 진우의 공개 채널입니다.");

    Channel channel = new Channel(
        "진우의 공개 채널",
        "안녕하세요. 진우의 공개 채널입니다.",
        ChannelType.PUBLIC);

    ChannelDto channelDto = ChannelDto.builder()
        .id(UUID.randomUUID())
        .name("진우의 공개 채널")
        .type(ChannelType.PUBLIC)
        .build();

    given(channelMapper.toEntity(any(ChannelCreatePublicRequest.class))).willReturn(channel);
    given(channelRepository.save(any(Channel.class))).willReturn(channel);
    given(channelMapper.toDto(any(Channel.class), anyList(), any()))
        .willReturn(channelDto);

    // when
    ChannelDto result = basicChannelService.createPublicChannel(request);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo("진우의 공개 채널");
    verify(channelRepository, times(1)).save(any(Channel.class));
  }

  @Test
  @DisplayName("비공개 채널 생성 성공: 유효한 정보가 입력되면 비공개 채널이 생성되어야 합니다.")
  void should_create_private_channel() {
    // given
    UUID userId1 = UUID.randomUUID();
    UUID userId2 = UUID.randomUUID();
    List<UUID> participantIds = List.of(userId1, userId2);

    ChannelCreatePrivateRequest request =
        new ChannelCreatePrivateRequest(participantIds);

    User user1 = new User("test1", "test1@email.com", "test1234");
    User user2 = new User("test2", "test2@email.com", "test1234");
    List<User> participants = List.of(user1, user2);

    Channel channel =
        new Channel(null, null, ChannelType.PRIVATE);

    ChannelDto channelDto = ChannelDto.builder()
        .id(UUID.randomUUID())
        .type(ChannelType.PRIVATE)
        .build();

    given(userRepository.findAllWithDetailsByIdIn(anyList())).willReturn(participants);
    given(channelRepository.save(any(Channel.class))).willReturn(channel);
    given(channelMapper.toDto(any(Channel.class), anyList(), any())).willReturn(channelDto);

    // when
    ChannelDto result = basicChannelService.createPrivateChannel(request);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getType()).isEqualTo(ChannelType.PRIVATE);
    verify(userRepository).findAllWithDetailsByIdIn(participantIds);
    verify(channelRepository, times(1)).save(any(Channel.class));
  }

  @Test
  @DisplayName("채널 수정 실패: 존재하지 않는 채널 ID인 경우 ChannelNotFoundException이 발생해야 합니다.")
  void should_throw_Exception_when_update_non_existent_channel_id() {
    // given
    UUID userId = UUID.randomUUID();

    ChannelUpdateRequest request = new ChannelUpdateRequest(
        "꼬야의 공개 체널",
        "안녕하세요. 꼬야의 공개 채널입니다.");

    given(channelRepository.findById(any())).willReturn(Optional.empty());

    // when, then
    assertThatThrownBy(() -> basicChannelService.update(userId, request))
        .isInstanceOf(ChannelNotFoundException.class);
    verify(channelRepository, never()).save(any(Channel.class));
    verify(channelMapper, never()).toDto(any(Channel.class), anyList(), any());
  }

  @Test
  @DisplayName("채널 삭제 실패: 존재하지 않는 채널 ID인 경우 ChannelNotFoundException이 발생해야 합니다.")
  void should_throw_exception_when_delete_non_existent_channel_id() {
    // given
    UUID userId = UUID.randomUUID();

    given(channelRepository.findById(userId)).willReturn(Optional.empty());

    // when, then
    assertThatThrownBy(() -> basicChannelService.delete(userId))
        .isInstanceOf(ChannelNotFoundException.class);
    verify(channelRepository, never()).delete(any(Channel.class));
  }
}