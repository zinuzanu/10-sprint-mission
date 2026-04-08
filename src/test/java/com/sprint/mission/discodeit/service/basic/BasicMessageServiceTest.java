package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasicMessageServiceTest {

  @Mock
  private MessageRepository messageRepository;
  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private BinaryContentRepository binaryContentRepository;
  @Mock
  private BinaryContentStorage binaryContentStorage;
  @Mock
  private MessageMapper messageMapper;
  @Mock
  private PageResponseMapper pageResponseMapper;

  @InjectMocks
  private BasicMessageService basicMessageService;

  @Test
  @DisplayName("메시지 생성 실패: 존재하지 않는 사용자 ID인 경우 UserNotFoundException이 발생해야 합니다.")
  void should_message_throw_exception_when_create_non_existent_user_id() {
    // given
    UUID userId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    MessageCreateRequest request =
        new MessageCreateRequest(userId, channelId, "안녕하세요!");

    given(userRepository.findById(any(UUID.class))).willReturn(Optional.empty());
    // when, then
    assertThatThrownBy(() -> basicMessageService.create(request, null))
        .isInstanceOf(UserNotFoundException.class);
    verify(channelRepository, never()).findById(channelId);
    verify(messageRepository, never()).saveAndFlush(any());
  }

  @Test
  @DisplayName("메시지 수정 성공: 올바른 요청 데이터가 전달되면 메시지 정보가 업데이트 되어야 합니다.")
  void should_update_success() {
    // given
    UUID messageId = UUID.randomUUID();
    MessageUpdateRequest request =
        new MessageUpdateRequest("수정되는 메시지 입니다.");

    User author = new User("test", "test@email.com", "test1234");
    Channel channel = new Channel("꼬야의 채널", "안녕하세요. 꼬야의 공개 채널입니다.", ChannelType.PUBLIC);

    Message message = new Message(
        author,
        channel,
        "기존 메시지 입니다.",
        new ArrayList<>());

    MessageDto messageDto = MessageDto.builder()
        .id(messageId)
        .content("수정되는 메시지 입니다.")
        .build();

    given(messageRepository.findWithAuthorAndAttachmentsById(messageId))
        .willReturn(Optional.of(message));
    given(messageMapper.toDto(any(Message.class))).willReturn(messageDto);

    // when
    MessageDto result = basicMessageService.update(messageId, request);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getContent()).isEqualTo("수정되는 메시지 입니다.");
    verify(messageRepository).findWithAuthorAndAttachmentsById(messageId);
    verify(messageMapper).toDto(message);
  }

  @Test
  @DisplayName("메시지 삭제 성공: 메시지가 존재하면 정상적으로 삭제가 되어야 합니다.")
  void should_delete_success() {
    // given
    UUID messageId = UUID.randomUUID();

    User author = new User("test", "test@email.com", "test1234");
    Channel channel = new Channel("꼬야의 채널", "안녕하세요. 꼬야의 공개 채널입니다.", ChannelType.PUBLIC);
    Message message = new Message(author, channel, "삭제될 메시지 입니다.", new ArrayList<>());

    given(messageRepository.findWithAuthorAndAttachmentsById(messageId))
        .willReturn(Optional.of(message));

    // when
    basicMessageService.delete(messageId);

    // then
    verify(messageRepository).findWithAuthorAndAttachmentsById(messageId);
    verify(messageRepository, times(1)).delete(message);
  }
}