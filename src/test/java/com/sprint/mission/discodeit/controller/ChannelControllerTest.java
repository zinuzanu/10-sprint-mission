package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.ChannelCreatePrivateRequest;
import com.sprint.mission.discodeit.dto.ChannelCreatePublicRequest;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ChannelController.class)
class ChannelControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private ChannelService channelService;

  @Test
  @DisplayName("공개 채널 생성 성공: 채널 이름을 입력하면 201 Created를 반환해야 합니다.")
  void createPublicChannel_success() throws Exception {
    // given
    ChannelCreatePublicRequest request = new ChannelCreatePublicRequest(
        "Public Channel", "누구나 접근 가능한 공개 채널");
    ChannelDto channelDto = ChannelDto.builder()
        .id(UUID.randomUUID())
        .name("Public Channel")
        .description("누구나 접근 가능한 공개 채널")
        .build();

    given(channelService.createPublicChannel(any())).willReturn(channelDto);

    // when, then
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("Public Channel"));
  }

  @Test
  @DisplayName("비공개 채널 생성 성공: 참여자 리스트를 입력하면 201 Created를 반환해야 합니다.")
  void createPrivateChannel_success() throws Exception {
    // given
    List<UUID> participantIds = List.of(UUID.randomUUID(), UUID.randomUUID());
    ChannelCreatePrivateRequest request = new ChannelCreatePrivateRequest(participantIds);

    ChannelDto channelDto = ChannelDto.builder()
        .id(UUID.randomUUID())
        .build();

    given(channelService.createPrivateChannel(any())).willReturn(channelDto);

    // when
    ResultActions actions = mockMvc.perform(post("/api/channels/private")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)));

    // then
    actions.andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists());
  }

  @Test
  @DisplayName("채널 수정 실패: 존재하지 않는 채널 ID로 수정 요청 시 400 Bad Request를 반환해야 합니다.")
  void update_fail() throws Exception {
    // given
    UUID id = UUID.randomUUID();
    ChannelUpdateRequest request = new ChannelUpdateRequest("New name", "New description");

    given(channelService.update(any(UUID.class), any(ChannelUpdateRequest.class)))
        .willThrow(new ChannelNotFoundException(id));

    // when
    ResultActions actions = mockMvc.perform(patch("/api/channels/{channelId}", id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)));

    // then
    actions.andExpect(status().isBadRequest());
    verify(channelService).update(any(UUID.class), any(ChannelUpdateRequest.class));
  }
}