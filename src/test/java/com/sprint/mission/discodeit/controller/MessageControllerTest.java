package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private MessageService messageService;

  @Test
  @DisplayName("메시지 생성 실패: 존재하지 않는 작성자로 ID를 요청하면 400 Bad Request를 반환해야 합니다.")
  void create_fail() throws Exception {
    // given
    UUID userId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    MessageCreateRequest request = new MessageCreateRequest(userId, channelId, "새로운 메시지");

    given(messageService.create(any(), any()))
        .willThrow(new UserNotFoundException(userId));

    MockMultipartFile requestPart = new MockMultipartFile(
        "messageCreateRequest",
        "",
        MediaType.APPLICATION_JSON_VALUE,
        objectMapper.writeValueAsBytes(request)
    );

    // when
    ResultActions actions = mockMvc.perform(
        multipart("/api/messages")
            .file(requestPart)
            .accept(MediaType.APPLICATION_JSON)
    );

    // then
    actions.andExpect(status().isBadRequest());
    verify(messageService).create(any(MessageCreateRequest.class), any());
  }

  @Test
  @DisplayName("메시지 수정 성공: 올바른 수정 요청을 보내면 200 OK와 수정된 메시지를 반환해야 합니다.")
  void update_success() throws Exception {
    // given
    UUID id = UUID.randomUUID();
    MessageUpdateRequest request = new MessageUpdateRequest("수정된 메시지");

    UserDto authorDto = UserDto.builder()
        .id(UUID.randomUUID())
        .username("test")
        .email("test@email.com")
        .build();

    MessageDto messageDto = MessageDto.builder()
        .id(id)
        .content("수정된 메시지")
        .author(authorDto)
        .channelId(UUID.randomUUID())
        .build();

    given(messageService.update(eq(id), any(MessageUpdateRequest.class)))
        .willReturn(messageDto);

    // when
    ResultActions actions = mockMvc.perform(
        patch("/api/messages/{messageId}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .accept(MediaType.APPLICATION_JSON)
    );

    // then
    actions.andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id.toString()))
        .andExpect(jsonPath("$.content").value("수정된 메시지"))
        .andExpect(jsonPath("$.author").exists())
        .andExpect(jsonPath("$.author.username").value("test"));

    verify(messageService).update(eq(id), any(MessageUpdateRequest.class));
  }
}