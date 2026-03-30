package com.sprint.mission.discodeit.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

public class MessageIntegrationTest extends BaseIntegrationTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ChannelRepository channelRepository;

  @Test
  @DisplayName("메시지 전송 시나리오: 사용자가 채널에 메시지를 보내면 목록에서 해당 내용을 확인할 수 있어야 합니다.")
  void message_create_and_list_success() throws Exception {
    // given
    User author = userRepository.save(new User("test", "test@email.com", "test1234"));
    Channel channel = channelRepository.save(
        new Channel("Public Channel", "누구나 접근 가능한 공개 채널", ChannelType.PUBLIC));

    MessageCreateRequest request = new MessageCreateRequest(
        author.getId(),
        channel.getId(),
        "테스트 입니다."
    );

    MockMultipartFile requestPart = new MockMultipartFile(
        "messageCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(request)
    );

    // when
    mockMvc.perform(multipart("/api/messages")
            .file(requestPart))
        .andExpect(status().isCreated());

    // then
    mockMvc.perform(get("/api/messages")
            .param("channelId", channel.getId().toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].content").value("테스트 입니다."))
        .andExpect(jsonPath("$.content[0].author.username").value("test"));
  }

  @Test
  @DisplayName("메시지 생성 예외: 내용이 비어있는 메시지를 전송할 경우 400 Bad Request를 반환해야 합니다.")
  void message_create_fail_empty_content() throws Exception {
    // given
    User author = userRepository.save(new User("tester", "test@test.com", "pass1234"));
    Channel channel = channelRepository.save(new Channel("테스트방", "설명", ChannelType.PUBLIC));

    MessageCreateRequest request = new MessageCreateRequest(author.getId(), channel.getId(), " ");
    MockMultipartFile requestPart = new MockMultipartFile(
        "messageCreateRequest", "", MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(request)
    );

    // when, then
    mockMvc.perform(multipart("/api/messages")
            .file(requestPart))
        .andExpect(status().isBadRequest());
  }
}
